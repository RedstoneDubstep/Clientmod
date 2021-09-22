package net.redstonedubstep.clientmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.redstonedubstep.clientmod.ClientSettings;

@Mixin(ActiveRenderInfo.class)
public abstract class MixinActiveRenderInfo {
	//Stop fluid fog + FOV effects from applying when inside a fluid
	@Inject(method = "getFluidState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockReader;getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;"), cancellable = true)
	private void onGetFluidState(CallbackInfoReturnable<FluidState> callbackInfo) {
		if (!ClientSettings.CONFIG.renderFluidEffects.get()) {
			callbackInfo.setReturnValue(Fluids.EMPTY.getDefaultState());
		}
	}
}
