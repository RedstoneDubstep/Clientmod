package net.redstonedubstep.clientmod.misc;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {
	public static KeyBinding openTextbox;

	public static void init() {
		openTextbox = new KeyBinding("key.clientmod.openTextbox", GLFW.GLFW_KEY_WORLD_2, "key.categories.clientmod");

		ClientRegistry.registerKeyBinding(openTextbox);
	}
}
