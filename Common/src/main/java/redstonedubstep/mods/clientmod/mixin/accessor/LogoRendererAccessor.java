package redstonedubstep.mods.clientmod.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.components.LogoRenderer;

@Mixin(LogoRenderer.class)
public interface LogoRendererAccessor {
    @Accessor()
    boolean getShowEasterEgg();
}
