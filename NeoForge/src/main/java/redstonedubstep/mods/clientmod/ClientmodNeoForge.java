package redstonedubstep.mods.clientmod;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import redstonedubstep.mods.clientmod.platform.NeoForgeClientSettings;

@Mod(ClientmodCommon.MOD_ID)
@Mod.EventBusSubscriber(modid = ClientmodCommon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientmodNeoForge {
	public ClientmodNeoForge() {
		ClientmodCommon.init();
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "", (a, b) -> b));
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, NeoForgeClientSettings.CLIENT_SPEC);
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
