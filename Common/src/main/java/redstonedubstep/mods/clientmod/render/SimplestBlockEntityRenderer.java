package redstonedubstep.mods.clientmod.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SimplestBlockEntityRenderer<T extends BlockEntity, S extends BlockEntityRenderState> implements BlockEntityRenderer<T, S> {
	@Override
	public S createRenderState() {
		return (S) new BlockEntityRenderState();
	}

	@Override
	public void submit(S renderState, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
		collector.submitCustomGeometry(poseStack, RenderTypes.lines(), (pose, consumer) -> {
			consumer.addVertex(pose, 0.0F, 0.0F, 0.0F).setLineWidth(0.2F).setColor(0xFF00FF00).setNormal(0.0F, 1.0F, 0.0F);
			consumer.addVertex(pose, 1.0F, 1.0F, 1.0F).setLineWidth(0.2F).setColor(0xFF00FF00).setNormal(0.0F, 1.0F, 0.0F);
		});
	}

	@Override
	public int getViewDistance() {
		return 32;
	}
}
