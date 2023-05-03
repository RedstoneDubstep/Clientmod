package redstonedubstep.mods.clientmod.mixin.clothconfig;

import java.util.concurrent.atomic.AtomicBoolean;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;

@Mixin(BooleanListEntry.class)
public interface BooleanListEntryAccessor {
    @Accessor
    AtomicBoolean getBool();
}
