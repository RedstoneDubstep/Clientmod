package net.redstonedubstep.clientmod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.redstonedubstep.clientmod.screen.button.SettingButton;

public class ClientSettings {
	public static final List<BetterBooleanOption> CONFIGS = new ArrayList<>();
	public static final List<BetterBooleanOption> SETTINGS = new ArrayList<>();

	private static boolean sendMessagesWithTeammsg = false;
	public static BetterBooleanOption SEND_MESSAGES_WITH_TEAMMSG = new BetterBooleanOption("sendMessagesWithTeammsg", () -> sendMessagesWithTeammsg, b -> sendMessagesWithTeammsg = b);

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
		public BooleanValue shouldReloadSounds;
		public BooleanValue drawReloadingBackground;
		public BooleanValue enhancedReloadingInfo;
		public BooleanValue renderFluidEffects;
		public BooleanValue renderEntitesGlowing;
		public BooleanValue nightVision;
		public BooleanValue betterFishingHook;

		Config(ForgeConfigSpec.Builder builder) {
			//for some reason we can't use language files in here, so comments are in english
			notifyWhenMinceraftScreen = register(builder
					.comment("Should Minecraft play a (loud) sound when the Minceraft logo is shown?")
					.define("notifyWhenMinceraftScreen", true));
			shouldReloadSounds = register(builder
					.comment("Should F3+T also reload sounds? (Turning this off will make resource reloading a bit faster)")
					.define("shouldReloadSounds", true));
			drawReloadingBackground = register(builder
					.comment("Should there be a red background when reloading resources with F3+T?")
					.define("drawReloadingBackground", true));
			enhancedReloadingInfo = register(builder
					.comment("Should there be additional information displayed (like the current task, the duration of the reload, etc.) while reloading resources?")
					.define("enhancedReloadingInfo", false));
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
		}

		private static BooleanValue register(BooleanValue value) {
			ClientSettings.CONFIGS.add(new BetterBooleanOption(value));
			return value;
		}

		private static BooleanValue register(BooleanValue value, Consumer<Boolean> onClick) {
			ClientSettings.CONFIGS.add(new BetterBooleanOption(value, onClick));
			return value;
		}
	}

	public static class BetterBooleanOption extends Option {
		public static final BetterBooleanOption EMPTY = new BetterBooleanOption("", "", null, null, false);
		private final Component tooltip;
		private final Supplier<Boolean> getter;
		private final Consumer<Boolean> setter;

		public BetterBooleanOption(BooleanValue config) {
			this(config, b -> {});
		}

		public BetterBooleanOption(BooleanValue config, Consumer<Boolean> onClick) {
			this("config.clientmod:" + config.getPath().get(0) + ".name", "config.clientmod:" + config.getPath().get(0) + ".description", config::get, b -> {
				config.set(b);
				onClick.accept(b);
			}, false);
		}

		public BetterBooleanOption(String settingName, Supplier<Boolean> getter, Consumer<Boolean> setter) {
			this("setting.clientmod:" + settingName + ".name", "setting.clientmod:" + settingName + ".description", getter, setter, true);
		}

		private BetterBooleanOption(String nameKey, String tooltipKey, Supplier<Boolean> getter, Consumer<Boolean> setter, boolean registerAsSetting) {
			super(nameKey);

			this.tooltip = !tooltipKey.isEmpty() ? new TranslatableComponent(tooltipKey) : null;
			this.getter = getter;
			this.setter = setter;

			if (registerAsSetting) {
				ClientSettings.SETTINGS.add(this);
			}
		}

		@Override
		public AbstractWidget createButton(Options options, int x, int y, int width) {
			return new SettingButton(x, y, width, 20, getCaption(), (b) -> setter.accept(!getter.get()), getter, tooltip);
		}

		public boolean getValue() {
			return getter.get();
		}
	}
}
