package redstonedubstep.mods.clientmod.mixin.rendering;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.client.renderer.entity.state.FishingHookRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.phys.AABB;
import redstonedubstep.mods.clientmod.ClientmodCommon;
import redstonedubstep.mods.clientmod.mixin.accessor.FishingHookAccessor;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

@Mixin(FishingHookRenderer.class)
public class FishingHookRendererMixin {
	//Render fishing hook differently depending on ability to catch treasure at current position
	@Redirect(method = "render(Lnet/minecraft/client/renderer/entity/state/FishingHookRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;", ordinal = 0))
	public VertexConsumer clientmod$redirectGetBuffer(MultiBufferSource instance, RenderType renderType, FishingHookRenderState renderState) {
		if (ClientSettings.INSTANCE.betterFishingHook()) {
			BlockPos hookPos = BlockPos.containing(renderState.x, renderState.y, renderState.z);
			List<FishingHook> fishingHooksAtPos = Minecraft.getInstance().level.getEntities(EntityType.FISHING_BOBBER, new AABB(hookPos), e -> true);

			if (!fishingHooksAtPos.isEmpty()) {
				FishingHook fishingHook = fishingHooksAtPos.getFirst();

				if (((FishingHookAccessor) fishingHook).invokeCalculateOpenWater(hookPos))
					renderType = RenderType.entityCutout(ResourceLocation.fromNamespaceAndPath(ClientmodCommon.MOD_ID, "textures/entity/fishing_hook_treasure.png"));
			}
		}

		return instance.getBuffer(renderType);
	}
}
