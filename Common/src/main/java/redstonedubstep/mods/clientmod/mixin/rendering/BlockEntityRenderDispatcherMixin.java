package redstonedubstep.mods.clientmod.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import redstonedubstep.mods.clientmod.misc.FieldHolder;
import redstonedubstep.mods.clientmod.render.SimplestBlockEntityRenderer;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {
	//Change BE renderers for select block entities to a much simpler renderer
	@Inject(method = "getRenderer", at = @At("HEAD"), cancellable = true)
	private <E extends BlockEntity> void clientmod$onGetRenderer(E be, CallbackInfoReturnable<BlockEntityRenderer<E>> callbackInfo) {
		if (FieldHolder.renderableBlockEntityFilter.contains(BlockEntityType.getKey(be.getType())))
			callbackInfo.setReturnValue(new SimplestBlockEntityRenderer<>());
	}
}
