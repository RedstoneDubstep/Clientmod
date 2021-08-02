package net.redstonedubstep.clientmod.misc;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

public class KeyBindings {
	public static KeyMapping openTextbox;

	public static void init() {
		openTextbox = new KeyMapping("key.clientmod:openTextbox.name", GLFW.GLFW_KEY_WORLD_2, "key.clientmod:category");

		ClientRegistry.registerKeyBinding(openTextbox);
	}
}
