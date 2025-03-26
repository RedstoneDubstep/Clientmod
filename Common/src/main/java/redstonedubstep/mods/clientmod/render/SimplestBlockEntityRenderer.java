package redstonedubstep.mods.clientmod.render;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class SimplestBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
	@Override
	public void render(T be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, Vec3 cameraPos) {
		VertexConsumer consumer = bufferSource.getBuffer(RenderType.lines());
		Matrix4f positionMatrix = poseStack.last().pose();

		consumer.addVertex(positionMatrix, 0.0F, 0.0F, 0.0F).setColor(0xFF00FF00).setNormal(0.0F, 1.0F, 0.0F);
		consumer.addVertex(positionMatrix, 1.0F, 1.0F, 1.0F).setColor(0xFF00FF00).setNormal(0.0F, 1.0F, 0.0F);
	}

	@Override
	public int getViewDistance() {
		return 32;
	}
}
