package redstonedubstep.mods.clientmod.mixin.accessor;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

@Mixin(Screen.class)
public interface ScreenAccessor {
    @Accessor
    List<GuiEventListener> getChildren();

    @Accessor
    List<NarratableEntry> getNarratables();

    @Accessor
    List<Renderable> getRenderables();
}
