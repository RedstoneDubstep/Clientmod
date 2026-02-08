package redstonedubstep.mods.clientmod.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin extends Projectile {
	private FishingHookMixin(EntityType<? extends Projectile> type, Level level) {
		super(type, level);
	}

	@Shadow
	protected abstract boolean calculateOpenWater(BlockPos pos);

	//Notifies fishing player that their fishing bobber has landed in treasure-eligible water
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;multiply(DDD)Lnet/minecraft/world/phys/Vec3;"))
	public void clientmod$onFishingBobberLandInWater(CallbackInfo callbackInfo) {
		if (ClientSettings.INSTANCE.betterFishingHook() && calculateOpenWater(blockPosition()) && getOwner() instanceof Player player)
			player.displayClientMessage(Component.translatable("messages.clientmod:betterFishingRod.openWater"), true);
	}
}
