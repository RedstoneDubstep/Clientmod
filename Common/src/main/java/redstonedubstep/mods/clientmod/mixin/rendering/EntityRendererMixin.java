package redstonedubstep.mods.clientmod.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
	//Let leashed mobs only render if they are within entity render distance
	@Redirect(method = "shouldRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Leashable;getLeashHolder()Lnet/minecraft/world/entity/Entity;"))
	private Entity clientmod$onShouldRender(Leashable leashable, Entity sameEntity, Frustum camera, double camX, double camY, double camZ) {
		Entity leashHolder = leashable.getLeashHolder();

		return leashHolder != null && (!ClientSettings.INSTANCE.reduceEntityDistance() || leashHolder.shouldRender(camX, camY, camZ)) ? leashHolder : null;
	}
}
