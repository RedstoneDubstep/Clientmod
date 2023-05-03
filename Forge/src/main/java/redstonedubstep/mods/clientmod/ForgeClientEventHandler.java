package redstonedubstep.mods.clientmod;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

@EventBusSubscriber(modid = ClientmodCommon.MOD_ID, value = Dist.CLIENT)
public class ForgeClientEventHandler {
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent event) {
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
			ClientEventHandler.onRenderGameOverlay(event.getPoseStack());
	}
}
