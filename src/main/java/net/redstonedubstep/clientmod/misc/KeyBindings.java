package net.redstonedubstep.clientmod.misc;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.redstonedubstep.clientmod.Clientmod;

@EventBusSubscriber(modid = Clientmod.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class KeyBindings {
	public static KeyMapping openTextbox;

	@SubscribeEvent
	public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
		openTextbox = new KeyMapping("key.clientmod:openTextbox.name", GLFW.GLFW_KEY_WORLD_2, "key.clientmod:category");

		event.register(openTextbox);
	}
}
