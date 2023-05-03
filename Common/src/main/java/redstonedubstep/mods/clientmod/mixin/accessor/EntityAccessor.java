package redstonedubstep.mods.clientmod.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.Entity;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Invoker()
    void invokeSetSharedFlag(int flag, boolean value);
}
