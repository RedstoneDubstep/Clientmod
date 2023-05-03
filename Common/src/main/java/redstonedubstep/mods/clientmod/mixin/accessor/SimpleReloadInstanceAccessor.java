package redstonedubstep.mods.clientmod.mixin.accessor;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.SimpleReloadInstance;

@Mixin(SimpleReloadInstance.class)
public interface SimpleReloadInstanceAccessor {
    @Accessor
    Set<PreparableReloadListener> getPreparingListeners();
}
