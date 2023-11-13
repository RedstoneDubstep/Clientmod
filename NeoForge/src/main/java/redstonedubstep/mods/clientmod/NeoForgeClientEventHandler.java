package redstonedubstep.mods.clientmod;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiOverlayEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.overlay.VanillaGuiOverlay;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

@Mod.EventBusSubscriber(modid = ClientmodCommon.MOD_ID, value = Dist.CLIENT)
public class NeoForgeClientEventHandler {
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START)
			ClientEventHandler.onClientTick();
	}

	@SubscribeEvent
	public static void onClickEvent(InputEvent.InteractionKeyMappingTriggered event) {
		if (event.isAttack() && ClientEventHandler.onAttackEvent())
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		ClientEventHandler.onRightClickBlock(event.getItemStack(), event.getPos());
	}

	@SubscribeEvent
	public static void onInitScreenPost(ScreenEvent.Init.Post event) {
		ClientEventHandler.onInitScreenPost(event.getScreen());
	}

	@SubscribeEvent
	public static void renderGameOverlayLayer(RenderGuiOverlayEvent.Pre event) {
		if (!ClientSettings.INSTANCE.renderSpyglassOverlay() && event.getOverlay() == VanillaGuiOverlay.SPYGLASS.type())
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onRenderGameOverlay(RenderGuiOverlayEvent.Post event) {
		if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type())
			ClientEventHandler.onRenderGameOverlay(event.getGuiGraphics());
	}
}
