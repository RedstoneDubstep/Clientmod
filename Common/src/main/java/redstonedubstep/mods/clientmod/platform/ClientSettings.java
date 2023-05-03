package redstonedubstep.mods.clientmod.platform;

import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ClientSettings {
    public static final Map<OptionInstance<Boolean>, Supplier<Boolean>> CONFIGS = new LinkedHashMap<>();
    public static final List<OptionInstance<Boolean>> SETTINGS = new ArrayList<>();
    public static final ClientSettings INSTANCE = Services.load(ClientSettings.class);
    public static OptionInstance<Boolean> SEND_MESSAGES_WITH_TEAMMSG = registerSetting("sendMessagesWithTeammsg", false);
    public static OptionInstance<Boolean> REDUCE_ENTITY_DISTANCE = registerSetting("reduceEntityDistance", false);

    //Configs
    public abstract boolean notifyWhenMinceraftScreen();

    public abstract boolean drawReloadingBackground();

    public abstract boolean enhancedReloadingInfo();

    public abstract boolean reloadFade();

    public abstract boolean reloadServerResources();

    public abstract boolean renderFluidEffects();

    public abstract boolean renderEntitiesGlowing();

    public abstract boolean nightVision();

    public abstract boolean betterFishingHook();

    public abstract boolean renderSpyglassOverlay();

    public abstract boolean invincibleVillagers();

    public abstract boolean enhancedItemInfo();

    public abstract boolean speedometer();

    public abstract boolean logShulkerPlacement();

    //Settings
    public abstract boolean sendMessagesWithTeammsg();

    public abstract boolean reduceEntityDistance();

    protected static void registerConfig(String id, Supplier<Boolean> getConfig, Consumer<Boolean> saveConfig, Consumer<Boolean> onClick) {
        OptionInstance<Boolean> option = OptionInstance.createBoolean("config.clientmod:" + id + ".name", OptionInstance.cachedConstantTooltip(Component.translatable("config.clientmod:" + id + ".description")), false, b -> {
            saveConfig.accept(b);
            onClick.accept(b);
        });

        ClientSettings.CONFIGS.put(option, getConfig);
    }

    private static OptionInstance<Boolean> registerSetting(String settingName, boolean defaultValue) {
        OptionInstance<Boolean> option = OptionInstance.createBoolean("setting.clientmod:" + settingName + ".name", OptionInstance.cachedConstantTooltip(Component.translatable("setting.clientmod:" + settingName + ".description")), defaultValue);

        ClientSettings.SETTINGS.add(option);
        return option;
    }

    public void updateOptionInstancesFromConfig() {
        for (Map.Entry<OptionInstance<Boolean>, Supplier<Boolean>> entry : ClientSettings.CONFIGS.entrySet()) {
            entry.getKey().set(entry.getValue().get());
        }
    }
}
