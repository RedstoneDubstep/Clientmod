package redstonedubstep.mods.clientmod.mixin.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

import java.util.Map;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Inject(method = "renderGuiItemDecorations(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = At.Shift.AFTER))
    public void clientmod$onRenderGuiItemDecorations(PoseStack poseStack, Font font, ItemStack stack, int x, int y, String indicator, CallbackInfo callbackInfo) {
        poseStack.pushPose();

        if (ClientSettings.INSTANCE.enhancedItemInfo()) {
            if (stack.is(Items.ENCHANTED_BOOK)) {
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
                int color = -1;
                boolean hasMaxEnchantment = false;

                for (Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
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
                RenderSystem.disableBlend();
                GuiComponent.fill(poseStack, x + 1, y + 1, x + 4, y + 4, color);
            }
            else if (stack.is(Items.BEE_NEST) || stack.is(Items.BEEHIVE)) {
                if (stack.hasTag()) {
                    ListTag tag = stack.getTag().getCompound("BlockEntityTag").getList("Bees", Tag.TAG_COMPOUND);

                    poseStack.translate(0.0D, 0.0D, 200.0F);
                    MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                    font.drawInBatch(String.valueOf(tag.size()), x + 8 - 2 - font.width(String.valueOf(tag.size())), y + 6 + 3, 0xFFD700, true, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
                    bufferSource.endBatch();
                }
            }
            else if (stack.getItem() instanceof ArmorItem || stack.is(ItemTags.TOOLS)) {
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
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
                RenderSystem.disableBlend();

                if (color >= 0)
                    GuiComponent.fill(poseStack, x + 1, y + 1, x + 4, y + 4, 0xFF000000 | color);
            }
        }

        poseStack.popPose();
    }
}
