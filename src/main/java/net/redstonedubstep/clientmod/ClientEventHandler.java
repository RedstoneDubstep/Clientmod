package net.redstonedubstep.clientmod;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.redstonedubstep.clientmod.command.CommandLibrary;
import net.redstonedubstep.clientmod.misc.KeyBindings;
import net.redstonedubstep.clientmod.screen.MainScreen;

@EventBusSubscriber(modid=Clientmod.MODID, bus=Bus.MOD, value=Dist.CLIENT)
public class ClientEventHandler {
	@SubscribeEvent
	public static void onFMLClientSetup(FMLClientSetupEvent event) {
		KeyBindings.init();
		CommandLibrary.addCommandsToList();
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if (KeyBindings.openTextbox.isPressed()) {
			Minecraft.getInstance().displayGuiScreen(new MainScreen());
		}
	}
}
