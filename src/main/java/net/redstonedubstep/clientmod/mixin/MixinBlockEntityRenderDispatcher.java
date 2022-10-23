package net.redstonedubstep.clientmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.redstonedubstep.clientmod.misc.FieldHolder;
import net.redstonedubstep.clientmod.render.SimplestBlockEntityRenderer;

@Mixin(BlockEntityRenderDispatcher.class)
public class MixinBlockEntityRenderDispatcher {
	@Inject(method = "getRenderer", at = @At("HEAD"), cancellable = true)
	private <E extends BlockEntity> void onGetRenderer(E be, CallbackInfoReturnable<BlockEntityRenderer<E>> callbackInfo) {
		if (FieldHolder.renderableBlockEntityFilter.contains(BlockEntityType.getKey(be.getType())))
			callbackInfo.setReturnValue(new SimplestBlockEntityRenderer<>());
	}
}
