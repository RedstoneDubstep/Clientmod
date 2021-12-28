package net.redstonedubstep.clientmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.FishingHook;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.Clientmod;

@Mixin(FishingHookRenderer.class)
public class MixinFishingHookRenderer {
	@Redirect(method = "render(Lnet/minecraft/world/entity/projectile/FishingHook;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;", ordinal = 0))
	public VertexConsumer redirectGetBuffer(MultiBufferSource instance, RenderType renderType, FishingHook entity) {
		if (ClientSettings.CONFIG.betterFishingHook.get() && entity.calculateOpenWater(entity.blockPosition()))
			renderType = RenderType.entityCutout(new ResourceLocation(Clientmod.MODID, "textures/entity/fishing_hook_treasure.png"));

		return instance.getBuffer(renderType);
	}
}
