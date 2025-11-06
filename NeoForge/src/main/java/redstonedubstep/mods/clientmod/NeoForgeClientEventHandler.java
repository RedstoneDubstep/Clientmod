package redstonedubstep.mods.clientmod;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

@EventBusSubscriber(modid = ClientmodCommon.MOD_ID, value = Dist.CLIENT)
public class NeoForgeClientEventHandler {
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Pre event) {
		ClientEventHandler.onClientTick();
	}

	@SubscribeEvent
	public static void onClickEvent(InputEvent.InteractionKeyMappingTriggered event) {
		if (event.isAttack() && ClientEventHandler.onAttackEvent())
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		ClientEventHandler.onRightClickBlock(event.getEntity(), event.getItemStack(), event.getPos());
	}

	@SubscribeEvent
	public static void onInitScreenPost(ScreenEvent.Init.Post event) {
		ClientEventHandler.onInitScreenPost(event.getScreen());
	}

	@SubscribeEvent
	public static void renderGameOverlayLayer(RenderGuiLayerEvent.Pre event) {
		if (!ClientSettings.INSTANCE.renderSpyglassOverlay() && event.getName().equals(VanillaGuiLayers.CAMERA_OVERLAYS))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onRenderGameOverlay(RenderGuiLayerEvent.Post event) {
		if (event.getName().equals(VanillaGuiLayers.CROSSHAIR))
			ClientEventHandler.onRenderGameOverlay(event.getGuiGraphics());
	}
}
