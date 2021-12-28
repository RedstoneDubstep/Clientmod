package net.redstonedubstep.clientmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.FishRenderer;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.ResourceLocation;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.Clientmod;

@Mixin(FishRenderer.class)
public class MixinFishRenderer {
	@Redirect(method = "render(Lnet/minecraft/entity/projectile/FishingBobberEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/IRenderTypeBuffer;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/IVertexBuilder;", ordinal = 0))
	public IVertexBuilder redirectGetBuffer(IRenderTypeBuffer instance, RenderType renderType, FishingBobberEntity entity) {
		if (ClientSettings.CONFIG.betterFishingHook.get() && entity.calculateOpenWater(entity.blockPosition()))
			renderType = RenderType.entityCutout(new ResourceLocation(Clientmod.MODID, "textures/entity/fishing_hook_treasure.png"));

		return instance.getBuffer(renderType);
	}
}
