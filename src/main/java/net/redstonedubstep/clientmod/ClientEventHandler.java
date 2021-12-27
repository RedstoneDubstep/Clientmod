package net.redstonedubstep.clientmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.event.HoverEvent.Action;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.redstonedubstep.clientmod.command.CommandException;
import net.redstonedubstep.clientmod.command.CommandLibrary;
import net.redstonedubstep.clientmod.misc.FieldHolder;
import net.redstonedubstep.clientmod.misc.KeyBindings;
import net.redstonedubstep.clientmod.misc.WaypointManager;
import net.redstonedubstep.clientmod.screen.MainScreen;

@EventBusSubscriber(modid = Clientmod.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if (KeyBindings.openTextbox.isDown()) {
			Minecraft.getInstance().setScreen(new MainScreen());
		}
	}

	//play a sound if the "Minceraft" logo is shown, credits to bl4ckscor3 for that code
	@SubscribeEvent
	public static void onInitGuiPost(InitGuiEvent.Post event) {
		if (ClientSettings.CONFIG.notifyWhenMinceraftScreen.get() && event.getGui() instanceof MainMenuScreen) {
			boolean isTitleWronglySpelled = ObfuscationReflectionHelper.getPrivateValue(MainMenuScreen.class, (MainMenuScreen)event.getGui(), "minceraftEasterEgg");

			if (isTitleWronglySpelled) {
				Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.WITHER_DEATH,1, 5));
			}
		}
	}

	@SubscribeEvent
	public static void onChatMessageSent(ClientChatEvent event) {
		if (ClientSettings.SEND_MESSAGES_WITH_TEAMMSG.getValue()) {
			if (!event.getMessage().startsWith("/")) {
				event.setMessage("/teammsg " + event.getMessage());
			}
		}
		else if (event.getMessage().startsWith("/clientmod ")) { //if you for some reason can't use the mod's screen
			String command = event.getMessage().replace("/clientmod ", "");
			CommandException result = CommandLibrary.parseAndExecuteCommand(command);

			if (result != null) {
				TranslationTextComponent errorMessage = new TranslationTextComponent("command.failed");

				errorMessage.withStyle(s -> s.withHoverEvent(new HoverEvent(Action.SHOW_TEXT, result.getFullDescription()))).withStyle(TextFormatting.RED);
				Minecraft.getInstance().player.sendMessage(errorMessage, Util.NIL_UUID);
			}

			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof PlayerEntity) {
			FieldHolder.lastDeathPosition = event.getEntity().blockPosition();
		}
	}

	@SubscribeEvent
	public static void onFMLLoadComplete(FMLLoadCompleteEvent event) {
		FieldHolder.isMinecraftStarting = false;
	}

	@SubscribeEvent
	public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		if(event.getType() == ElementType.CROSSHAIRS) {
			PlayerEntity player = Minecraft.getInstance().player;
			Vector3d lookVec = player.getLookAngle().multiply(1, 0, 1).normalize();

			if (WaypointManager.getInstance().hasWaypoint()) {
				Vector3d relativePos = Vector3d.atLowerCornerOf(WaypointManager.getInstance().getWaypoint().subtract(player.blockPosition()));
				float dotProductRaw = (float)lookVec.dot(relativePos.multiply(1, 0, 1).normalize());
				float dotProduct = Math.max(0, dotProductRaw - 0.7F);
				int relativeHeight = Math.round((float)relativePos.y());
				int yOffset = relativeHeight > 3 ? -10 : (relativeHeight < -3 ? 10 : 0);
				int squareSize = relativePos.length() < 150 ? 20 : 10;
				int startX = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - squareSize / 2;
				int startY = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - squareSize / 2 + yOffset;
				int alpha = Math.round(0x11 * dotProduct * 36) << 24;
				int color = alpha + (dotProduct > 0.299 ? 0xFFA500 : 0xFF0000); //default red, orange when the direction is correct

				if (dotProductRaw == 0) {
					color = 0x6600FF00; //green marker when position is reached
				}

				AbstractGui.fill(event.getMatrixStack(), startX, startY, startX + squareSize, startY + squareSize, color);
			}
		}
	}
}
