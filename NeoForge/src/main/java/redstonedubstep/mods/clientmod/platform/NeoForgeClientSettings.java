package redstonedubstep.mods.clientmod.platform;

import java.util.function.Consumer;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import redstonedubstep.mods.clientmod.mixin.accessor.EntityAccessor;

public class NeoForgeClientSettings extends ClientSettings {
	@Override
	public boolean notifyWhenMinceraftScreen() {
		return CONFIG.notifyWhenMinceraftScreen.get();
	}

	@Override
	public boolean drawReloadingBackground() {
		return CONFIG.drawReloadingBackground.get();
	}

	@Override
	public boolean enhancedReloadingInfo() {
		return CONFIG.enhancedReloadingInfo.get();
	}

	@Override
	public boolean reloadFade() {
		return CONFIG.reloadFade.get();
	}

	@Override
	public boolean reloadServerResources() {
		return CONFIG.reloadServerResources.get();
	}

	@Override
	public boolean renderFluidEffects() {
		return CONFIG.renderFluidEffects.get();
	}

	@Override
	public boolean renderEntitiesGlowing() {
		return CONFIG.renderEntitiesGlowing.get();
	}

	@Override
	public boolean nightVision() {
		return CONFIG.nightVision.get();
	}

	@Override
	public boolean betterFishingHook() {
		return CONFIG.betterFishingHook.get();
	}

	@Override
	public boolean renderSpyglassOverlay() {
		return CONFIG.renderSpyglassOverlay.get();
	}

	@Override
	public boolean invincibleVillagers() {
		return CONFIG.invincibleVillagers.get();
	}

	@Override
	public boolean enhancedItemInfo() {
		return CONFIG.enhancedItemInfo.get();
	}

	@Override
	public boolean speedometer() {
		return CONFIG.speedometer.get();
	}

	@Override
	public boolean logShulkerPlacement() {
		return CONFIG.logShulkerPlacement.get();
	}

	//config-related stuff
	public static final ModConfigSpec CLIENT_SPEC;
	public static final Config CONFIG;

	static {
		final Pair<Config, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Config::new);
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
		public BooleanValue renderEntitiesGlowing;
		public BooleanValue nightVision;
		public BooleanValue betterFishingHook;
		public BooleanValue renderSpyglassOverlay;
		public BooleanValue invincibleVillagers;
		public BooleanValue enhancedItemInfo;
		public BooleanValue speedometer;
		public BooleanValue logShulkerPlacement;

		Config(ModConfigSpec.Builder builder) {
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
			renderEntitiesGlowing = register(builder
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
			enhancedItemInfo = register(builder
					.comment("Should certain items, like enchanted books, have additional information about their data displayed?")
					.define("enhancedItemInfo", true));
			speedometer = register(builder
					.comment("Should your current speed be displayed next to your hotbar?")
					.define("speedometer", false));
			logShulkerPlacement = register(builder
					.comment("Should the position of placed shulker boxes be logged?")
					.define("logShulkerPlacement", true));
		}

		private static BooleanValue register(BooleanValue config) {
			return register(config, b -> {});
		}

		private static BooleanValue register(BooleanValue config, Consumer<Boolean> onClick) {
			registerConfig(config.getPath().get(0), config, config::set, onClick);
			return config;
		}
	}

}
