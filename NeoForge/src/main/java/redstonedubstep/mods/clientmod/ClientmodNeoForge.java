package redstonedubstep.mods.clientmod;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import redstonedubstep.mods.clientmod.platform.NeoForgeClientSettings;

@Mod(ClientmodCommon.MOD_ID)
@EventBusSubscriber(modid = ClientmodCommon.MOD_ID)
public class ClientmodNeoForge {
	public ClientmodNeoForge(ModContainer container) {
		ClientmodCommon.init();
		container.registerConfig(ModConfig.Type.CLIENT, NeoForgeClientSettings.CLIENT_SPEC);
	}

	@SubscribeEvent
	public static void onFMLLoadComplete(FMLLoadCompleteEvent event) {
		ClientmodCommon.onClientStarted();
	}

	@SubscribeEvent
	public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(ClientmodCommon.openTextbox);
	}
}
