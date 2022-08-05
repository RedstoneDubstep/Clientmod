package net.redstonedubstep.clientmod.mixin;

import java.util.Map;
import java.util.Map.Entry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;

import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.redstonedubstep.clientmod.ClientSettings;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
	@Shadow public float blitOffset;
	@Shadow protected abstract void fillRect(BufferBuilder renderer, int xX, int y, int width, int height, int red, int green, int blue, int alpha);

	@Inject(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isBarVisible()Z"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void onRenderItemDecorations(Font font, ItemStack stack, int x, int y, String text, CallbackInfo callbackInfo) {
		if (ClientSettings.CONFIG.enhancedItemInfo.get()) {
			if (stack.is(Items.ENCHANTED_BOOK)) {
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
				fillRect(bufferBuilder, x + 1, y + 1, 3, 3, color >> 16 & 255, color >> 8 & 255, color & 255, 255);
				RenderSystem.enableBlend();
				RenderSystem.enableTexture();
				RenderSystem.enableDepthTest();
			}
			else if (stack.is(Items.BEEHIVE) && stack.hasTag()) {
				PoseStack pose = new PoseStack();
				ListTag tag = stack.getTag().getCompound("BlockEntityTag").getList("Bees", Tag.TAG_COMPOUND);

				pose.translate(0.0D, 0.0D, blitOffset + 200.0F);
				MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
				font.drawInBatch(tag.size() + "", x + 8 - 2 - font.width(tag.size() + ""), y + 6 + 3, 0xFFD700, true, pose.last().pose(), multibuffersource$buffersource, false, 0, 15728880);
				multibuffersource$buffersource.endBatch();
			}
		}
	}
}
