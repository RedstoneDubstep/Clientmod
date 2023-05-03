package redstonedubstep.mods.clientmod.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.TitleScreen;

@Mixin(TitleScreen.class)
public interface TitleScreenAccessor {
    @Accessor()
    LogoRenderer getLogoRenderer();
}
