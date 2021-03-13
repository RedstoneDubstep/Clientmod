package net.redstonedubstep.clientmod.misc;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {
	public static KeyBinding openTextbox;

	public static void init() {
		openTextbox = new KeyBinding("key.clientmod:openTextbox.name", GLFW.GLFW_KEY_WORLD_2, "key.clientmod:category");

		ClientRegistry.registerKeyBinding(openTextbox);
	}
}
