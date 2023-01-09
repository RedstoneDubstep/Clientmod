package net.redstonedubstep.clientmod;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.IItemDecorator;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper.UnableToFindFieldException;
import net.minecraftforge.registries.ForgeRegistries;
import net.redstonedubstep.clientmod.misc.ClientUtility;
import net.redstonedubstep.clientmod.misc.FieldHolder;
import net.redstonedubstep.clientmod.misc.KeyBindings;
import net.redstonedubstep.clientmod.misc.WaypointManager;
import net.redstonedubstep.clientmod.screen.MainScreen;

@EventBusSubscriber(modid = Clientmod.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
	private static final Logger LOGGER = LogManager.getLogger();

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if (KeyBindings.openTextbox.consumeClick())
			Minecraft.getInstance().setScreen(new MainScreen());
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.player instanceof LocalPlayer player && event.phase == Phase.START) {
			if (ClientSettings.CONFIG.renderEntitesGlowing.get()) {
				for (Entity entity : ((ClientLevel) player.level).entitiesForRendering()) {
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
	public static void onClickEvent(InputEvent.InteractionKeyMappingTriggered event) {
		if (ClientSettings.CONFIG.invincibleVillagers.get() && event.isAttack() && !Minecraft.getInstance().gameMode.getPlayerMode().isCreative() && Minecraft.getInstance().hitResult instanceof EntityHitResult hitResult && hitResult.getEntity() instanceof Villager)
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (ClientSettings.CONFIG.logShulkerPlacement.get() && event.getItemStack().getItem() instanceof BlockItem item && item.getBlock() instanceof ShulkerBoxBlock)
			LOGGER.info("Placed " + ForgeRegistries.BLOCKS.getKey(item.getBlock()) + " at " + ClientUtility.formatBlockPos(event.getPos()));
	}

	//play a sound if the "Minceraft" logo is shown, credits to bl4ckscor3 for that code
	@SubscribeEvent
	public static void onInitScreenPost(ScreenEvent.Init.Post event) {
		if (ClientSettings.CONFIG.notifyWhenMinceraftScreen.get() && event.getScreen() instanceof TitleScreen screen) {
			try {
				boolean isTitleWronglySpelled = ObfuscationReflectionHelper.getPrivateValue(TitleScreen.class, screen, "f_96720_");

				if (isTitleWronglySpelled)
					Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.WITHER_DEATH, 1, 5));
			}
			catch (UnableToFindFieldException e) {
				e.printStackTrace();
			}
		}
	}

	@SubscribeEvent
	public static void onFMLLoadComplete(FMLLoadCompleteEvent event) {
		FieldHolder.isMinecraftStarting = false;
	}

	@SubscribeEvent
	public static void renderGameOverlayLayer(RenderGuiOverlayEvent.Pre event) {
		if (!ClientSettings.CONFIG.renderSpyglassOverlay.get() && event.getOverlay() == VanillaGuiOverlay.SPYGLASS.type())
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onRenderGameOverlay(RenderGuiOverlayEvent.Post event) {
		if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()) {
			Player player = Minecraft.getInstance().player;
			Vec3 lookVec = player.getLookAngle().multiply(1, 0, 1).normalize();

			if (WaypointManager.getInstance().hasWaypoint()) {
				Vec3 relativePos = Vec3.atLowerCornerOf(WaypointManager.getInstance().getWaypoint().subtract(player.blockPosition()));
				float dotProductRaw = (float) lookVec.dot(relativePos.multiply(1, 0, 1).normalize());
				float dotProduct = Math.max(0, dotProductRaw - 0.7F);
				int relativeHeight = Math.round((float) relativePos.y());
				int yOffset = relativeHeight > 3 ? -10 : (relativeHeight < -3 ? 10 : 0);
				int squareSize = relativePos.length() < 150 ? 20 : 10;
				int startX = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - squareSize / 2;
				int startY = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - squareSize / 2 + yOffset;
				int alpha = Math.round(0x11 * dotProduct * 30) << 24;
				int color = alpha + (dotProduct > 0.299 ? 0xFFA500 : 0xFF0000); //default red, orange when the direction is correct

				if (dotProductRaw == 0) {
					color = 0x9900FF00; //green marker when position is reached
				}

				GuiComponent.fill(event.getPoseStack(), startX, startY, startX + squareSize, startY + squareSize, color);
			}
		}
		else if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type() && ClientSettings.CONFIG.speedometer.get()) {
			LocalPlayer player = Minecraft.getInstance().player;
			double velocity = player.position().subtract(player.xo, player.yo, player.zo).length() * 20;
			int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
			int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();

			Minecraft.getInstance().font.draw(event.getPoseStack(), String.format("%.2f b/s", velocity), (width + 182) / 2 + 10, height - 16, 0xFFFFFF);
		}
	}

	@SubscribeEvent
	public static void onScreenInit(ScreenEvent.Init.Post event) {
		if (event.getScreen() instanceof MerchantScreen screen) {
			Minecraft minecraft = screen.getMinecraft();
			Button closeButton = new ExtendedButton(screen.getGuiLeft() + screen.inventoryLabelX + 50, screen.getGuiTop() + screen.inventoryLabelY, 10, 10, Component.literal("X"), b -> minecraft.player.closeContainer());
			event.addListener(closeButton);
		}
	}

	public static void registerItemDecorations(BiConsumer<ItemLike, IItemDecorator> consumer) {
		consumer.accept(Items.ENCHANTED_BOOK, (font, stack, x, y, blit) -> {
			if (ClientSettings.CONFIG.enhancedItemInfo.get()) {
				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
				Tesselator tesselator = Tesselator.getInstance();
				BufferBuilder bufferBuilder = tesselator.getBuilder();
				int color = -1;
				boolean hasMaxEnchantment = false;

				for (Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
					if (enchantment.getKey().getMaxLevel() <= enchantment.getValue()) {
						hasMaxEnchantment = true;
						break;
					}
				}

				if (enchantments.size() > 1)
					color = hasMaxEnchantment ? 0xFF00FFFF : 0xFFFF8C00;
				else if (enchantments.size() == 1)
					color = hasMaxEnchantment ? 0xFF00FF00 : 0xFFFF0000;

				RenderSystem.disableDepthTest();
				RenderSystem.disableTexture();
				RenderSystem.disableBlend();
				Minecraft.getInstance().getItemRenderer().fillRect(bufferBuilder, x + 1, y + 1, 3, 3, color >> 16 & 255, color >> 8 & 255, color & 255, 255);
			}

			return true;
		});

		IItemDecorator beeIndicatorDecorator = (font, stack, x, y, blit) -> {
			if (ClientSettings.CONFIG.enhancedItemInfo.get() && stack.hasTag()) {
				PoseStack pose = new PoseStack();
				ListTag tag = stack.getTag().getCompound("BlockEntityTag").getList("Bees", Tag.TAG_COMPOUND);

				pose.translate(0.0D, 0.0D, blit + 200.0F);
				MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
				font.drawInBatch(tag.size() + "", x + 8 - 2 - font.width(tag.size() + ""), y + 6 + 3, 0xFFD700, true, pose.last().pose(), multibuffersource$buffersource, false, 0, 15728880);
				multibuffersource$buffersource.endBatch();
			}

			return false;
		};
		IItemDecorator equipmentEnchantmentDecorator = (font, stack, x, y, blit) -> {
			if (ClientSettings.CONFIG.enhancedItemInfo.get()) {
				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
				Tesselator tesselator = Tesselator.getInstance();
				BufferBuilder bufferBuilder = tesselator.getBuilder();
				int color = -1;

				if (enchantments.containsKey(Enchantments.ALL_DAMAGE_PROTECTION))
					color = 0x696969;
				else if (enchantments.containsKey(Enchantments.BLAST_PROTECTION))
					color = 0x53CF43;
				else if (enchantments.containsKey(Enchantments.FIRE_PROTECTION))
					color = 0xFF7514;
				else if (enchantments.containsKey(Enchantments.PROJECTILE_PROTECTION))
					color = 0xDFDFDF;

				if (enchantments.containsKey(Enchantments.SILK_TOUCH))
					color = 0xFDDA0D;
				else if (enchantments.containsKey(Enchantments.BLOCK_FORTUNE))
					color = 0x4CBB17;

				if (enchantments.containsKey(Enchantments.SHARPNESS))
					color = 0xA9A9A9;
				else if (enchantments.containsKey(Enchantments.SMITE))
					color = 0x006400;
				else if (enchantments.containsKey(Enchantments.BANE_OF_ARTHROPODS))
					color = 0x964B00;

				if (enchantments.containsKey(Enchantments.LOYALTY))
					color = 0x1434A4;
				else if (enchantments.containsKey(Enchantments.RIPTIDE))
					color = 0x7DF9FF;

				RenderSystem.disableDepthTest();
				RenderSystem.disableTexture();
				RenderSystem.disableBlend();

				if (color >= 0)
					Minecraft.getInstance().getItemRenderer().fillRect(bufferBuilder, x + 1, y + 1, 3, 3, color >> 16 & 255, color >> 8 & 255, color & 255, 255);
			}

			return true;
		};

		consumer.accept(Items.BEEHIVE, beeIndicatorDecorator);
		consumer.accept(Items.BEE_NEST, beeIndicatorDecorator);
		consumer.accept(Items.DIAMOND_HELMET, equipmentEnchantmentDecorator);
		consumer.accept(Items.DIAMOND_CHESTPLATE, equipmentEnchantmentDecorator);
		consumer.accept(Items.DIAMOND_LEGGINGS, equipmentEnchantmentDecorator);
		consumer.accept(Items.DIAMOND_BOOTS, equipmentEnchantmentDecorator);
		consumer.accept(Items.NETHERITE_HELMET, equipmentEnchantmentDecorator);
		consumer.accept(Items.NETHERITE_CHESTPLATE, equipmentEnchantmentDecorator);
		consumer.accept(Items.NETHERITE_LEGGINGS, equipmentEnchantmentDecorator);
		consumer.accept(Items.NETHERITE_BOOTS, equipmentEnchantmentDecorator);
		consumer.accept(Items.DIAMOND_AXE, equipmentEnchantmentDecorator);
		consumer.accept(Items.DIAMOND_HOE, equipmentEnchantmentDecorator);
		consumer.accept(Items.DIAMOND_PICKAXE, equipmentEnchantmentDecorator);
		consumer.accept(Items.DIAMOND_SHOVEL, equipmentEnchantmentDecorator);
		consumer.accept(Items.DIAMOND_SWORD, equipmentEnchantmentDecorator);
		consumer.accept(Items.NETHERITE_AXE, equipmentEnchantmentDecorator);
		consumer.accept(Items.NETHERITE_HOE, equipmentEnchantmentDecorator);
		consumer.accept(Items.NETHERITE_PICKAXE, equipmentEnchantmentDecorator);
		consumer.accept(Items.NETHERITE_SHOVEL, equipmentEnchantmentDecorator);
		consumer.accept(Items.NETHERITE_SWORD, equipmentEnchantmentDecorator);
		consumer.accept(Items.TRIDENT, equipmentEnchantmentDecorator);
	}
}
