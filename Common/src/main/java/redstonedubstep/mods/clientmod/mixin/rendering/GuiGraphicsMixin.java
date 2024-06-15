package redstonedubstep.mods.clientmod.mixin.rendering;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Shadow @Final private PoseStack pose;

    @Shadow public abstract int drawString(Font $$0, @Nullable String $$1, int $$2, int $$3, int $$4);

    @Shadow public abstract void fill(RenderType $$0, int $$1, int $$2, int $$3, int $$4, int $$5);

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = At.Shift.AFTER))
    public void clientmod$onRenderGuiItemDecorations(Font font, ItemStack stack, int x, int y, String subText, CallbackInfo callbackInfo) {
        pose.pushPose();

        if (ClientSettings.INSTANCE.enhancedItemInfo()) {
            if (stack.is(Items.ENCHANTED_BOOK)) {
                ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack);
                int color = -1;
                boolean hasMaxEnchantment = false;

                for (Object2IntMap.Entry<Holder<Enchantment>> enchantment : enchantments.entrySet()) {
                    if (enchantment.getKey().value().getMaxLevel() <= enchantment.getIntValue()) {
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
                fill(RenderType.guiOverlay(), x + 1, y + 1, x + 4, y + 4, color);
            }
            else if (stack.is(Items.BEE_NEST) || stack.is(Items.BEEHIVE)) {
                List<BeehiveBlockEntity.Occupant> bees = stack.get(DataComponents.BEES);

                if (bees != null) {
                    pose.translate(0.0D, 0.0D, 200.0F);
                    drawString(font, String.valueOf(bees.size()), x + 8 - 2 - font.width(String.valueOf(bees.size())), y + 6 + 3, 0xFFD700);
                }
            }
            else if (stack.getItem() instanceof ArmorItem || stack.is(ItemTags.BREAKS_DECORATED_POTS)) {
                List<Enchantment> enchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack).entrySet().stream().map(e -> e.getKey().value()).toList();
                int color = -1;

                if (enchantments.contains(Enchantments.PROTECTION))
                    color = 0x696969;
                else if (enchantments.contains(Enchantments.BLAST_PROTECTION))
                    color = 0x53CF43;
                else if (enchantments.contains(Enchantments.FIRE_PROTECTION))
                    color = 0xFF7514;
                else if (enchantments.contains(Enchantments.PROJECTILE_PROTECTION))
                    color = 0xDFDFDF;

                if (enchantments.contains(Enchantments.SILK_TOUCH))
                    color = 0xFDDA0D;
                else if (enchantments.contains(Enchantments.FORTUNE))
                    color = 0x4CBB17;

                if (enchantments.contains(Enchantments.SHARPNESS))
                    color = 0xA9A9A9;
                else if (enchantments.contains(Enchantments.SMITE))
                    color = 0x006400;
                else if (enchantments.contains(Enchantments.BANE_OF_ARTHROPODS))
                    color = 0x964B00;

                if (enchantments.contains(Enchantments.LOYALTY))
                    color = 0x1434A4;
                else if (enchantments.contains(Enchantments.RIPTIDE))
                    color = 0x7DF9FF;

                RenderSystem.disableDepthTest();
                RenderSystem.disableBlend();

                if (color >= 0)
                    fill(RenderType.guiOverlay(), x + 1, y + 1, x + 4, y + 4, 0xFF000000 | color);
            }
        }

        pose.popPose();
    }
}
