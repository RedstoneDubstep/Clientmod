package net.redstonedubstep.clientmod;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ClientSettings {

	public static boolean sendMessagesWithTeammsg = false;

	//config-related stuff
	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Config CONFIG;

	static {
		final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
		CLIENT_SPEC = specPair.getRight();
		CONFIG = specPair.getLeft();
	}

	public static class Config
	{
		public BooleanValue notifyWhenMinceraftScreen;
		public BooleanValue shouldReloadSounds;
		public BooleanValue drawReloadingBackground;

		Config(ForgeConfigSpec.Builder builder)
		{
			//for some reason we can't use language files in here, so comments are in english
			notifyWhenMinceraftScreen = builder
					.comment("Should Minecraft play a (loud) sound when the Minceraft logo is shown?")
					.define("notifyWhenMinceraftScreen", true);
			shouldReloadSounds = builder
					.comment("Should F3+T also reload sounds? (Turning this off will make resource reloading a bit faster)")
					.define("shouldReloadSounds", true);
			drawReloadingBackground = builder
					.comment("Should there be a red background when reloading resources with F3+T?")
					.define("drawReloadingBackground", true);
		}
	}
}
