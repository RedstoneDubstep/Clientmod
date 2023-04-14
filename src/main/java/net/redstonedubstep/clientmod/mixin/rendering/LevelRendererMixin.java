package net.redstonedubstep.clientmod.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.renderer.LevelRenderer;
import net.redstonedubstep.clientmod.ClientSettings;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	//Reduce the radius most entities stop rendering at by a factor of 10
	@ModifyArg(method = "updateRenderChunks", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setViewScale(D)V"))
	private double clientmod$redirectSetEntityViewScale(double renderDistWeight) {
		if (ClientSettings.REDUCE_ENTITY_DISTANCE.get())
			renderDistWeight /= 10;

		return renderDistWeight;
	}
}
