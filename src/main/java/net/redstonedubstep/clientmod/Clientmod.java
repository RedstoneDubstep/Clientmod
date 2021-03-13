package net.redstonedubstep.clientmod;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.redstonedubstep.clientmod.command.CommandLibrary;
import net.redstonedubstep.clientmod.misc.KeyBindings;

@Mod(Clientmod.MODID)
@EventBusSubscriber(modid=Clientmod.MODID, bus=Bus.MOD)
public class Clientmod {
	public static final String MODID = "clientmod";

	public Clientmod() {
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}

	@SubscribeEvent
	public static void onFMLClientSetup(FMLClientSetupEvent event) {
		KeyBindings.init();
		CommandLibrary.addCommandsToList();
	}
}
