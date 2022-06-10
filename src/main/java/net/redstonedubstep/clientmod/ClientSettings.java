package net.redstonedubstep.clientmod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ClientSettings {
	public static final Map<OptionInstance<Boolean>, BooleanValue> CONFIGS = new LinkedHashMap<>();
	public static final List<OptionInstance<Boolean>> SETTINGS = new ArrayList<>();

	public static OptionInstance<Boolean> SEND_MESSAGES_WITH_TEAMMSG = registerSetting("sendMessagesWithTeammsg", false);

	//config-related stuff
	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Config CONFIG;

	static {
		final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
		CLIENT_SPEC = specPair.getRight();
		CONFIG = specPair.getLeft();
	}

	public static class Config {
		public BooleanValue notifyWhenMinceraftScreen;
		public BooleanValue drawReloadingBackground;
		public BooleanValue enhancedReloadingInfo;
		public BooleanValue reloadFade;
		public BooleanValue reloadServerResources;
		public BooleanValue renderFluidEffects;
		public BooleanValue renderEntitesGlowing;
		public BooleanValue nightVision;
		public BooleanValue betterFishingHook;
		public BooleanValue renderSpyglassOverlay;
		public BooleanValue invincibleVillagers;

		Config(ForgeConfigSpec.Builder builder) {
			//for some reason we can't use language files in here, so comments are in english
			notifyWhenMinceraftScreen = register(builder
					.comment("Should Minecraft play a (loud) sound when the Minceraft logo is shown?")
					.define("notifyWhenMinceraftScreen", true));
			drawReloadingBackground = register(builder
					.comment("Should there be a red background when reloading resources with F3+T?")
					.define("drawReloadingBackground", true));
			enhancedReloadingInfo = register(builder
					.comment("Should there be additional information displayed (like the current task, the duration of the reload, etc.) while reloading resources?")
					.define("enhancedReloadingInfo", false));
			reloadFade = register(builder
					.comment("Should the reload overlay fade in and out for two seconds before and after reloading is complete?")
					.define("reloadFade", true));
			reloadServerResources = register(builder
					.comment("Should server resource packs get automatically loaded into the client when joining a server?")
					.define("reloadServerResources", true));
			renderFluidEffects = register(builder
					.comment("Should the water/lava fog and the respective FOV change be visible while being inside a fluid?")
					.define("renderFluidEffects", true));
			renderEntitesGlowing = register(builder
					.comment("Should entities in the player's vicinity appear as glowing?")
					.define("renderEntitiesGlowing", false), b -> {
				if (!b)
					Minecraft.getInstance().level.entitiesForRendering().forEach(e -> {
						if (e instanceof LivingEntity entity && entity.hasEffect(MobEffects.GLOWING) && entity.getEffect(MobEffects.GLOWING).getDuration() <= 0) {
							entity.removeEffect(MobEffects.GLOWING);
							entity.setSharedFlag(6, false);
						}
					});
			});
			nightVision = register(builder
					.comment("Should the player be able to see as if he has Night Vision?")
					.define("nightVision", false));
			betterFishingHook = register(builder
					.comment("Should a Fishing Hook change its texture when it is in open water?")
					.define("betterFishingHook", false));
			renderSpyglassOverlay = register(builder
					.comment("Should the Spyglass overlay be rendered when scoping through a Spyglass?")
					.define("renderSpyglassOverlay", true));
			invincibleVillagers = register(builder
					.comment("Should Villagers be invincible to direct player attacks?")
					.define("invincibleVillagers", false));
		}

		private static BooleanValue register(BooleanValue config) {
			return register(config, b -> {});
		}

		private static BooleanValue register(BooleanValue config, Consumer<Boolean> onClick) {
			OptionInstance<Boolean> option = OptionInstance.createBoolean("config.clientmod:" + config.getPath().get(0) + ".name", OptionInstance.cachedConstantTooltip(Component.translatable("config.clientmod:" + config.getPath().get(0) + ".description")), false, b -> {
				config.set(b);
				onClick.accept(b);
			});

			ClientSettings.CONFIGS.put(option, config);
			return config;
		}
	}

	private static OptionInstance<Boolean> registerSetting(String settingName, boolean defaultValue) {
		OptionInstance<Boolean> option = OptionInstance.createBoolean("setting.clientmod:" + settingName + ".name", OptionInstance.cachedConstantTooltip(Component.translatable("setting.clientmod:" + settingName + ".description")), defaultValue);

		ClientSettings.SETTINGS.add(option);
		return option;
	}

	public static void updateConfigSettingValues() {
		for (Map.Entry<OptionInstance<Boolean>, BooleanValue> entry : ClientSettings.CONFIGS.entrySet()) {
			entry.getKey().set(entry.getValue().get());
		}
	}
}
