package redstonedubstep.mods.clientmod;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import redstonedubstep.mods.clientmod.platform.ForgeClientSettings;

@Mod(ClientmodCommon.MOD_ID)
@EventBusSubscriber(modid = ClientmodCommon.MOD_ID, bus = Bus.MOD)
public class ClientmodForge {
	public ClientmodForge() {
		ClientmodCommon.init();
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "", (a, b) -> b));
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ForgeClientSettings.CLIENT_SPEC);
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
