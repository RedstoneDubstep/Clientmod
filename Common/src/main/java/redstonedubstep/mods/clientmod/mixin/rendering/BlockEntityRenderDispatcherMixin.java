package redstonedubstep.mods.clientmod.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import redstonedubstep.mods.clientmod.misc.FieldHolder;
import redstonedubstep.mods.clientmod.render.SimplestBlockEntityRenderer;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {
	//Change BE renderers for select block entities to a much simpler renderer
	@Inject(method = "getRenderer(Lnet/minecraft/world/level/block/entity/BlockEntity;)Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderer;", at = @At("HEAD"), cancellable = true)
	private <E extends BlockEntity, S extends BlockEntityRenderState> void clientmod$onGetRendererFromBE(E be, CallbackInfoReturnable<BlockEntityRenderer<E, S>> callbackInfo) {
		if (FieldHolder.renderableBlockEntityFilter.contains(Identifier.tryParse(be.typeHolder().getRegisteredName())))
			callbackInfo.setReturnValue(new SimplestBlockEntityRenderer<>());
	}

	//Change BE renderers for select block entities to a much simpler renderer
	@Inject(method = "getRenderer(Lnet/minecraft/client/renderer/blockentity/state/BlockEntityRenderState;)Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderer;", at = @At("HEAD"), cancellable = true)
	private <E extends BlockEntity, S extends BlockEntityRenderState> void clientmod$onGetRendererFromRenderState(S state, CallbackInfoReturnable<BlockEntityRenderer<E, S>> callbackInfo) {
		if (FieldHolder.renderableBlockEntityFilter.contains(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(state.blockEntityType)))
			callbackInfo.setReturnValue(new SimplestBlockEntityRenderer<>());
	}
}
