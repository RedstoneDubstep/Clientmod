package net.redstonedubstep.clientmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.network.FMLNetworkConstants;
import net.redstonedubstep.clientmod.command.CommandLibrary;
import net.redstonedubstep.clientmod.misc.KeyBindings;

@Mod(Clientmod.MODID)
@EventBusSubscriber(modid=Clientmod.MODID, bus=Bus.MOD)
public class Clientmod {
	public static final String MODID = "clientmod";

	public Clientmod() {
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientSettings.CLIENT_SPEC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEventHandler::onFMLLoadComplete);
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}

	@SubscribeEvent
	public static void onFMLClientSetup(FMLClientSetupEvent event) {
		KeyBindings.init();
		CommandLibrary.addCommandsToList();
	}
}
