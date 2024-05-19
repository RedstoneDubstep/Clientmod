package redstonedubstep.mods.clientmod.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.Entity;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
	//Reduce the radius most entities stop rendering at by a factor of 10
	@ModifyArg(method = "setupRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setViewScale(D)V"))
	private double clientmod$redirectSetEntityViewScale(double renderDistWeight) {
		if (ClientSettings.INSTANCE.reduceEntityDistance())
			renderDistWeight /= 10;

		return renderDistWeight;
	}

	//Fixes the logic determining whether a player moved considering the player's x pos instead of the camera's x pos
	@WrapOperation(method = "setupRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getX()D"))
	private double fixCameraXLocation(LocalPlayer player, Operation<Double> original) {
		if (ClientSettings.INSTANCE.fixSpectatingChunks()) {
			 Entity camera = Minecraft.getInstance().getCameraEntity();

			 if (camera != null)
				 return camera.getX();
		}

		return original.call(player);
	}

	//Fixes the logic determining whether a player moved considering the player's y pos instead of the camera's y pos
	@WrapOperation(method = "setupRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getY()D"))
	private double fixCameraYLocation(LocalPlayer player, Operation<Double> original) {
		if (ClientSettings.INSTANCE.fixSpectatingChunks()) {
			Entity camera = Minecraft.getInstance().getCameraEntity();

			if (camera != null)
				return camera.getZ();
		}

		return original.call(player);
	}

	//Fixes the logic determining whether a player moved considering the player's z pos instead of the camera's z pos
	@WrapOperation(method = "setupRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getZ()D"))
	private double fixCameraZLocation(LocalPlayer player, Operation<Double> original) {
		if (ClientSettings.INSTANCE.fixSpectatingChunks()) {
			Entity camera = Minecraft.getInstance().getCameraEntity();

			if (camera != null)
				return camera.getZ();
		}

		return original.call(player);
	}
}