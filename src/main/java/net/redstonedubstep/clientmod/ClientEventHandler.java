package net.redstonedubstep.clientmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.redstonedubstep.clientmod.misc.FieldHolder;
import net.redstonedubstep.clientmod.misc.KeyBindings;
import net.redstonedubstep.clientmod.screen.MainScreen;

@EventBusSubscriber(modid = Clientmod.MODID)
public class ClientEventHandler {
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if (KeyBindings.openTextbox.isPressed()) {
			Minecraft.getInstance().displayGuiScreen(new MainScreen());
		}
	}

	//play a sound if the "Minceraft" logo is shown, credits to bl4ckscor3 for that code
	@SubscribeEvent
	public static void onInitGuiPost(InitGuiEvent.Post event) {
		if(event.getGui() instanceof MainMenuScreen) {
			boolean isTitleWronglySpelled = ObfuscationReflectionHelper.getPrivateValue(MainMenuScreen.class, (MainMenuScreen)event.getGui(), "field_213101_e");

			if (isTitleWronglySpelled) {
				Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.ENTITY_WITHER_DEATH,1, 5));
			}
		}
	}

	@SubscribeEvent
	public static void onFMLLoadComplete(FMLLoadCompleteEvent event) {
		FieldHolder.isMinecraftStarting = false;
	}
}
