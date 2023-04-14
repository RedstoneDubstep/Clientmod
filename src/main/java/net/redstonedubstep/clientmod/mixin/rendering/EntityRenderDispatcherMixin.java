package net.redstonedubstep.clientmod.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.redstonedubstep.clientmod.misc.FieldHolder;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
	//Change BE renderers for select block entities to a much simpler renderer
	@Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
	private <E extends Entity> void clientmod$onShouldRender(E entity, Frustum frustum, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (FieldHolder.renderableEntityFilter.contains(entity.getType()))
			callbackInfo.setReturnValue(false);
	}
}
