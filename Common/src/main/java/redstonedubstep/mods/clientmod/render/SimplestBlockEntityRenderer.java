package redstonedubstep.mods.clientmod.render;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SimplestBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
	@Override
	public void render(T be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		VertexConsumer consumer = bufferSource.getBuffer(RenderType.lines());
		Matrix4f positionMatrix = poseStack.last().pose();

		consumer.vertex(positionMatrix, 0.0F, 0.0F, 0.0F).color(0xFF00FF00).normal(0.0F, 1.0F, 0.0F).endVertex();
		consumer.vertex(positionMatrix, 1.0F, 1.0F, 1.0F).color(0xFF00FF00).normal(0.0F, 1.0F, 0.0F).endVertex();
	}

	@Override
	public int getViewDistance() {
		return 32;
	}
}
