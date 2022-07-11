package net.redstonedubstep.clientmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.VibrationSignalParticle;

@Mixin(VibrationSignalParticle.class)
public abstract class MixinVibrationSignalParticle extends Particle {
	protected MixinVibrationSignalParticle(ClientLevel level, double x, double y, double z) {
		super(level, x, y, z);
	}

	//Calls setPos when the particle moves, so the bounding box updates accordingly and the particle gets rendered
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;atan2(DD)D"))
	public void onTick(CallbackInfo callbackInfo) {
		setPos(x, y, z);
	}
}
