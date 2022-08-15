package net.redstonedubstep.clientmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.misc.FieldHolder;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
	//Entity needs a constructor, so here we go
	private MixinLivingEntity(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	//Save the delta vector that gets used for movement, so it can be used to display the speed next to the hotbar when the Speedometer is enabled
	@Inject(method = "handleRelativeFrictionAndCalculateMovement", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void onSetProperDeltaMovement(Vec3 oldDeltaMovement, float friction, CallbackInfoReturnable<Vec3> callbackInfo, Vec3 deltaMovement) {
		if (ClientSettings.CONFIG.speedometer.get() && level.isClientSide) {
			FieldHolder.deltaMovement = deltaMovement;
		}
	}
}
