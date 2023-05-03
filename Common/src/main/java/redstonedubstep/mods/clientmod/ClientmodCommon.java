package redstonedubstep.mods.clientmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import redstonedubstep.mods.clientmod.command.CommandLibrary;
import redstonedubstep.mods.clientmod.misc.FieldHolder;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class ClientmodCommon {
    public static final String MOD_ID = "clientmod";
    public static final Logger LOGGER = LogManager.getLogger();
    public static KeyMapping openTextbox = new KeyMapping("key.clientmod:openTextbox.name", GLFW.GLFW_KEY_WORLD_2, "key.clientmod:category");

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        CommandLibrary.addCommandsToList();
    }

    public static void onClientStarted() {
        FieldHolder.isMinecraftStarting = false;
    }
}