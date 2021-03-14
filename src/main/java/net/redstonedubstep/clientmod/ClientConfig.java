package net.redstonedubstep.clientmod;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ClientConfig {

	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Client CLIENT;

	static {
		final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	public static class Client
	{
		public BooleanValue shouldReloadSounds;
		public BooleanValue drawReloadingBackground;

		Client(ForgeConfigSpec.Builder builder)
		{
			//for some reason we can't use language files in here, so comments are in english
			shouldReloadSounds = builder
					.comment("Should F3+T also reload sounds? (Turning this off will make resource reloading a bit faster)")
					.define("shouldReloadSounds", true);
			drawReloadingBackground = builder
					.comment("Should there be a red background when reloading resources with F3+T?")
					.define("drawReloadingBackground", true);
		}
	}
}
