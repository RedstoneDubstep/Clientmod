package redstonedubstep.mods.clientmod.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

@Mixin(MobRenderer.class)
public class MobRendererMixin {
	//Let leashed mobs only render if they are within entity render distance
	@Redirect(method = "shouldRender(Lnet/minecraft/world/entity/Mob;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;getLeashHolder()Lnet/minecraft/world/entity/Entity;"))
	private Entity clientmod$onShouldRender(Mob entity, Mob livingEntity, Frustum camera, double camX, double camY, double camZ) {
		Entity leashHolder = entity.getLeashHolder();

		return leashHolder != null && (!ClientSettings.INSTANCE.reduceEntityDistance() || leashHolder.shouldRender(camX, camY, camZ)) ? leashHolder : null;
	}
}
