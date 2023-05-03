package redstonedubstep.mods.clientmod.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.FishingHook;

@Mixin(FishingHook.class)
public interface FishingHookAccessor {
    @Invoker()
    boolean invokeCalculateOpenWater(BlockPos pos);
}
