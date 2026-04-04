package redstonedubstep.mods.clientmod.mixin.rendering;

import java.util.List;

import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Bees;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsExtractorMixin {
    @Shadow @Final private Matrix3x2fStack pose;

    @Shadow public abstract void text(Font font, String str, int x, int y, int color);

    @Shadow public abstract void fill(int x0, int y0, int x1, int y1, int col);

    @Inject(method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;popMatrix()Lorg/joml/Matrix3x2fStack;", shift = At.Shift.AFTER))
    public void clientmod$onRenderGuiItemDecorations(Font font, ItemStack stack, int x, int y, String countText, CallbackInfo callbackInfo) {
        pose.pushMatrix();

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

                fill(x + 1, y + 1, x + 4, y + 4, color);
            }
            else if (stack.is(Items.BEE_NEST) || stack.is(Items.BEEHIVE)) {
                Bees beesComponent = stack.get(DataComponents.BEES);
                List<BeehiveBlockEntity.Occupant> bees = beesComponent != null ? beesComponent.bees() : null;

                if (bees != null)
                    text(font, String.valueOf(bees.size()), x + 8 - 2 - font.width(String.valueOf(bees.size())), y + 6 + 3, 0xFFFFD700);
            }
            else if (stack.has(DataComponents.EQUIPPABLE) || stack.has(DataComponents.WEAPON) || stack.has(DataComponents.TOOL)) {
                try {
                    List<Enchantment> enchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack).entrySet().stream().map(e -> e.getKey().value()).toList();
                    RegistryLookup<Enchantment> enchantmentRegistry = Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                    int color = -1;

                    if (enchantments.contains(enchantmentRegistry.getOrThrow(Enchantments.PROTECTION).value()))
                        color = 0x696969;
                    else if (enchantments.contains(enchantmentRegistry.getOrThrow(Enchantments.BLAST_PROTECTION).value()))
                        color = 0x53CF43;
                    else if (enchantments.contains(enchantmentRegistry.getOrThrow(Enchantments.FIRE_PROTECTION).value()))
                        color = 0xFF7514;
                    else if (enchantments.contains(enchantmentRegistry.getOrThrow(Enchantments.PROJECTILE_PROTECTION).value()))
                        color = 0xDFDFDF;

                    if (enchantments.contains(enchantmentRegistry.getOrThrow(Enchantments.SILK_TOUCH).value()))
                        color = 0xFDDA0D;
                    else if (enchantments.contains(enchantmentRegistry.getOrThrow(Enchantments.FORTUNE).value()))
                        color = 0x4CBB17;

                    if (enchantments.contains(enchantmentRegistry.getOrThrow(Enchantments.SHARPNESS).value()))
                        color = 0xA9A9A9;
                    else if (enchantments.contains(enchantmentRegistry.getOrThrow(Enchantments.SMITE).value()))
                        color = 0x006400;
                    else if (enchantments.contains(enchantmentRegistry.getOrThrow(Enchantments.BANE_OF_ARTHROPODS).value()))
                        color = 0x964B00;

                    if (enchantments.contains(enchantmentRegistry.getOrThrow(Enchantments.LOYALTY).value()))
                        color = 0x1434A4;
                    else if (enchantments.contains(enchantmentRegistry.getOrThrow(Enchantments.RIPTIDE).value()))
                        color = 0x7DF9FF;

                    if (color >= 0)
                        fill(x + 1, y + 1, x + 4, y + 4, 0xFF000000 | color);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        pose.popMatrix();
    }
}
