package net.redstonedubstep.clientmod;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.InputEvent.ClickInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper.UnableToFindFieldException;
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
		if (KeyBindings.openTextbox.consumeClick()) {
			Minecraft.getInstance().setScreen(new MainScreen());
		}
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.player instanceof LocalPlayer player) {
			if (ClientSettings.CONFIG.renderEntitesGlowing.get()) {
				for (Entity entity : ((ClientLevel)player.level).entitiesForRendering()) {
					if (entity instanceof LivingEntity living) {
						living.addEffect(new MobEffectInstance(MobEffects.GLOWING));
						living.setSharedFlag(6, true);
					}
				}
			}

			if (ClientSettings.CONFIG.nightVision.get())
				player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 210));
			else if (player.hasEffect(MobEffects.NIGHT_VISION) && player.getEffect(MobEffects.NIGHT_VISION).getDuration() <= 0)
				player.removeEffect(MobEffects.NIGHT_VISION);

			if (player.deathTime == 1)
				FieldHolder.lastDeathPosition = player.blockPosition();
		}
	}

	@SubscribeEvent
	public static void onClickEvent(ClickInputEvent event) {
		if (ClientSettings.CONFIG.invincibleVillagers.get() && event.isAttack() && !Minecraft.getInstance().gameMode.getPlayerMode().isCreative() && Minecraft.getInstance().hitResult instanceof EntityHitResult hitResult && hitResult.getEntity() instanceof Villager)
			event.setCanceled(true);
	}

	//play a sound if the "Minceraft" logo is shown, credits to bl4ckscor3 for that code
	@SubscribeEvent
	public static void onInitGuiPost(ScreenEvent.InitScreenEvent.Post event) {
		if (ClientSettings.CONFIG.notifyWhenMinceraftScreen.get() && event.getScreen() instanceof TitleScreen screen) {
			try {
				boolean isTitleWronglySpelled = ObfuscationReflectionHelper.getPrivateValue(TitleScreen.class, screen, "f_96720_");

				if (isTitleWronglySpelled) {
					Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.WITHER_DEATH, 1, 5));
				}
			} catch(UnableToFindFieldException e) {
				e.printStackTrace();
			}
		}
	}

	@SubscribeEvent
	public static void onChatMessageSent(ClientChatEvent event) {
		if (ClientSettings.SEND_MESSAGES_WITH_TEAMMSG.get()) {
			if (!event.getMessage().startsWith("/")) {
				event.setMessage("/teammsg " + event.getMessage());
			}
		}
		else if (event.getMessage().startsWith("/clientmod ")) { //if you for some reason can't use the mod's screen
			String command = event.getMessage().replace("/clientmod ", "");
			CommandException result = CommandLibrary.parseAndExecuteCommand(command);

			if (result != null) {
				MutableComponent errorMessage = Component.translatable("command.failed");

				errorMessage.withStyle(s -> s.withHoverEvent(new HoverEvent(Action.SHOW_TEXT, result.getFullDescription()))).withStyle(ChatFormatting.RED);
				Minecraft.getInstance().player.sendSystemMessage(errorMessage);
			}

			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onFMLLoadComplete(FMLLoadCompleteEvent event) {
		FieldHolder.isMinecraftStarting = false;
	}

	@SubscribeEvent
	public static void renderGameOverlayLayer(RenderGameOverlayEvent.PreLayer event) {
		if (!ClientSettings.CONFIG.renderSpyglassOverlay.get() && event.getOverlay() == ForgeIngameGui.SPYGLASS_ELEMENT)
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		if(event.getType() == ElementType.LAYER) {
			Player player = Minecraft.getInstance().player;
			Vec3 lookVec = player.getLookAngle().multiply(1, 0, 1).normalize();

			if (WaypointManager.getInstance().hasWaypoint()) {
				Vec3 relativePos = Vec3.atLowerCornerOf(WaypointManager.getInstance().getWaypoint().subtract(player.blockPosition()));
				float dotProductRaw = (float)lookVec.dot(relativePos.multiply(1, 0, 1).normalize());
				float dotProduct = Math.max(0, dotProductRaw - 0.7F);
				int relativeHeight = Math.round((float)relativePos.y());
				int yOffset = relativeHeight > 3 ? -10 : (relativeHeight < -3 ? 10 : 0);
				int squareSize = relativePos.length() < 150 ? 20 : 10;
				int startX = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - squareSize / 2;
				int startY = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - squareSize / 2 + yOffset;
				int alpha = Math.round(0x11 * dotProduct * 5) << 24;
				int color = alpha + (dotProduct > 0.299 ? 0xFFA500 : 0xFF0000); //default red, orange when the direction is correct

				if (dotProductRaw == 0) {
					color = 0x6600FF00; //green marker when position is reached
				}

				GuiComponent.fill(event.getPoseStack(), startX, startY, startX + squareSize, startY + squareSize, color);
			}
		}
	}

	@SubscribeEvent
	public static void onScreenInit(ScreenEvent.InitScreenEvent.Post event) {
		if (event.getScreen() instanceof MerchantScreen screen) {
			Minecraft minecraft = screen.getMinecraft();
			Button closeButton = new ExtendedButton(screen.getGuiLeft() + screen.inventoryLabelX + 50, screen.getGuiTop() + screen.inventoryLabelY, 10, 10, Component.literal("X"), b -> minecraft.player.closeContainer());
			event.addListener(closeButton);
		}
	}
}
