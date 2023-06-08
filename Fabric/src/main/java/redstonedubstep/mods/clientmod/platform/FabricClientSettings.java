package redstonedubstep.mods.clientmod.platform;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import redstonedubstep.mods.clientmod.ClientmodCommon;
import redstonedubstep.mods.clientmod.mixin.accessor.EntityAccessor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FabricClientSettings extends ClientSettings {
    @Override
    public boolean notifyWhenMinceraftScreen() {
        return notifyWhenMinceraftScreen.get();
    }

    @Override
    public boolean drawReloadingBackground() {
        return drawReloadingBackground.get();
    }

    @Override
    public boolean enhancedReloadingInfo() {
        return enhancedReloadingInfo.get();
    }

    @Override
    public boolean reloadFade() {
        return reloadFade.get();
    }

    @Override
    public boolean reloadServerResources() {
        return reloadServerResources.get();
    }

    @Override
    public boolean renderFluidEffects() {
        return renderFluidEffects.get();
    }

    @Override
    public boolean renderEntitiesGlowing() {
        return renderEntitiesGlowing.get();
    }

    @Override
    public boolean nightVision() {
        return nightVision.get();
    }

    @Override
    public boolean betterFishingHook() {
        return betterFishingHook.get();
    }

    @Override
    public boolean renderSpyglassOverlay() {
        return renderSpyglassOverlay.get();
    }

    @Override
    public boolean invincibleVillagers() {
        return invincibleVillagers.get();
    }

    @Override
    public boolean enhancedItemInfo() {
        return enhancedItemInfo.get();
    }

    @Override
    public boolean speedometer() {
        return speedometer.get();
    }

    @Override
    public boolean logShulkerPlacement() {
        return logShulkerPlacement.get();
    }

    @Override
    public boolean sendMessagesWithTeammsg() {
        return SEND_MESSAGES_WITH_TEAMMSG.get();
    }

    @Override
    public boolean reduceEntityDistance() {
        return REDUCE_ENTITY_DISTANCE.get();
    }

    BooleanConfig notifyWhenMinceraftScreen = register(new BooleanConfig()
            .comment("Should Minecraft play a (loud) sound when the Minceraft logo is shown?")
            .define("notifyWhenMinceraftScreen", true));
    BooleanConfig drawReloadingBackground = register(new BooleanConfig()
            .comment("Should there be a red background when reloading resources with F3+T?")
            .define("drawReloadingBackground", true));
    BooleanConfig enhancedReloadingInfo = register(new BooleanConfig()
            .comment("Should there be additional information displayed (like the current task, the duration of the reload, etc.) while reloading resources?")
            .define("enhancedReloadingInfo", false));
    BooleanConfig reloadFade = register(new BooleanConfig()
            .comment("Should the reload overlay fade in and out for two seconds before and after reloading is complete?")
            .define("reloadFade", true));
    BooleanConfig reloadServerResources = register(new BooleanConfig()
            .comment("Should server resource packs get automatically loaded into the client when joining a server?")
            .define("reloadServerResources", true));
    BooleanConfig renderFluidEffects = register(new BooleanConfig()
            .comment("Should the water/lava fog and the respective FOV change be visible while being inside a fluid?")
            .define("renderFluidEffects", true));
    BooleanConfig renderEntitiesGlowing = register(new BooleanConfig()
            .comment("Should entities in the player's vicinity appear as glowing?")
            .define("renderEntitiesGlowing", false), b -> {
        if (!b)
            Minecraft.getInstance().level.entitiesForRendering().forEach(e -> {
                if (e instanceof LivingEntity entity && entity.hasEffect(MobEffects.GLOWING) && entity.getEffect(MobEffects.GLOWING).getDuration() <= 0) {
                    entity.removeEffect(MobEffects.GLOWING);
                    ((EntityAccessor) entity).invokeSetSharedFlag(6, false);
                }
            });
    });
    BooleanConfig nightVision = register(new BooleanConfig()
            .comment("Should the player be able to see as if he has Night Vision?")
            .define("nightVision", false));
    BooleanConfig betterFishingHook = register(new BooleanConfig()
            .comment("Should a Fishing Hook change its texture when it is in open water?")
            .define("betterFishingHook", false));
    BooleanConfig renderSpyglassOverlay = register(new BooleanConfig()
            .comment("Should the Spyglass overlay be rendered when scoping through a Spyglass?")
            .define("renderSpyglassOverlay", true));
    BooleanConfig invincibleVillagers = register(new BooleanConfig()
            .comment("Should Villagers be invincible to direct player attacks?")
            .define("invincibleVillagers", false));
    BooleanConfig enhancedItemInfo = register(new BooleanConfig()
            .comment("Should certain items, like enchanted books, have additional information about their data displayed?")
            .define("enhancedItemInfo", true));
    BooleanConfig speedometer = register(new BooleanConfig()
            .comment("Should your current speed be displayed next to your hotbar?")
            .define("speedometer", false));
    BooleanConfig logShulkerPlacement = register(new BooleanConfig()
            .comment("Should the position of placed shulker boxes be logged?")
            .define("logShulkerPlacement", true));

    private static BooleanConfig register(BooleanConfig config) {
        return register(config, b -> {
        });
    }

    private static BooleanConfig register(BooleanConfig config, Consumer<Boolean> onClick) {
        Consumer<Boolean> setValue = b -> {
            config.set(b);
            saveConfig();
        };

        registerConfig(config.id, config, setValue, onClick);
        return config;
    }

    public void loadConfig() {
        Path configFile = FabricLoader.getInstance().getConfigDir().resolve(ClientmodCommon.MOD_ID + ".toml");

        if (Files.exists(configFile)) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile.toFile())))) {
                reader.lines().forEach(s -> {
                    if (!s.startsWith("#")) {
                        String[] idAndValue = s.split(" = ");
                        String id = idAndValue[0];

                        if (idAndValue.length > 1) {
                            boolean value = Boolean.parseBoolean(idAndValue[1]);

                            for (Supplier<Boolean> supplier : CONFIGS.values()) {
                                if (supplier instanceof BooleanConfig config && config.id.equals(id)) {
                                    config.value = value;
                                    break;
                                }
                            }
                        }
                    }
                });
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveConfig() {
        Path configFile = FabricLoader.getInstance().getConfigDir().resolve(ClientmodCommon.MOD_ID + ".toml");

        try (OutputStreamWriter outputStream = new OutputStreamWriter(new FileOutputStream(configFile.toFile()))) {
            Files.createDirectories(configFile.getParent());

            for (Supplier<Boolean> supplier : CONFIGS.values()) {
                if (supplier instanceof BooleanConfig config) {
                    outputStream.write("# " + config.comment + "\n");
                    outputStream.write(config.id + " = " + config.value + "\n");
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class BooleanConfig implements Supplier<Boolean> {
        private boolean value;
        private String id;
        private boolean defaultValue;
        private String comment;

        public BooleanConfig(String id, boolean defaultValue, String comment) {
            this.value = defaultValue;
            this.id = id;
            this.defaultValue = defaultValue;
            this.comment = comment;
        }

        public BooleanConfig() {
        }

        @Override
        public Boolean get() {
            return value;
        }

        public void set(boolean value) {
            this.value = value;
        }

        public BooleanConfig comment(String comment) {
            this.comment = comment;
            return this;
        }

        public BooleanConfig define(String id, boolean defaultValue) {
            this.id = id;
            this.defaultValue = defaultValue;
            this.value = defaultValue;
            return this;
        }
    }
}
