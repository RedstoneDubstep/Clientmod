package net.redstonedubstep.clientmod;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.util.text.TranslationTextComponent;
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

		Client(ForgeConfigSpec.Builder builder)
		{
			shouldReloadSounds = builder
					.comment(new TranslationTextComponent("Should F3+T also reload sounds? (Turning this off will make resource reloading significantly faster)").getString())
					.define("shouldReloadSounds", true);
		}
	}
}
