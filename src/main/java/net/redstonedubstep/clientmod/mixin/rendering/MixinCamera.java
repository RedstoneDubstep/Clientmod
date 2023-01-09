package net.redstonedubstep.clientmod.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Camera;
import net.minecraft.world.level.material.FogType;
import net.redstonedubstep.clientmod.ClientSettings;

@Mixin(Camera.class)
public abstract class MixinCamera {
	//Stop fluid fog + FOV effects from applying when inside a fluid
	@Inject(method = "getFluidInCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/BlockGetter;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;"), cancellable = true)
	private void onGetFluidInCamera(CallbackInfoReturnable<FogType> callbackInfo) {
		if (!ClientSettings.CONFIG.renderFluidEffects.get())
			callbackInfo.setReturnValue(FogType.NONE);
	}
}

