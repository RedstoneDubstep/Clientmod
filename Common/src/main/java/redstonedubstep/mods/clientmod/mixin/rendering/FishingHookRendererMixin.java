package redstonedubstep.mods.clientmod.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.FishingHook;
import redstonedubstep.mods.clientmod.platform.ClientSettings;
import redstonedubstep.mods.clientmod.ClientmodCommon;
import redstonedubstep.mods.clientmod.mixin.accessor.FishingHookAccessor;

@Mixin(FishingHookRenderer.class)
public class FishingHookRendererMixin {
	//Render fishing hook differently depending on ability to catch treasure at current position
	@Redirect(method = "render(Lnet/minecraft/world/entity/projectile/FishingHook;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;", ordinal = 0))
	public VertexConsumer clientmod$redirectGetBuffer(MultiBufferSource instance, RenderType renderType, FishingHook fishingHook) {
		if (ClientSettings.INSTANCE.betterFishingHook() && ((FishingHookAccessor) fishingHook).invokeCalculateOpenWater(fishingHook.blockPosition()))
			renderType = RenderType.entityCutout(new ResourceLocation(ClientmodCommon.MOD_ID, "textures/entity/fishing_hook_treasure.png"));

		return instance.getBuffer(renderType);
	}
}
