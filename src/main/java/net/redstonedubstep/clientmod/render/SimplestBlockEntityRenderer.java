package net.redstonedubstep.clientmod.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.model.data.ModelData;

public class SimplestBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
	@Override
	public void render(T be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.WARPED_BUTTON.defaultBlockState());

		Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(RenderType.solid()), Blocks.WARPED_BUTTON.defaultBlockState(), model, 0, 0, 0, packedLight, packedOverlay, ModelData.EMPTY, null);
	}

	@Override
	public int getViewDistance() {
		return 32;
	}
}
