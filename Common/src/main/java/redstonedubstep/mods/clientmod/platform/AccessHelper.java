package redstonedubstep.mods.clientmod.platform;

import java.nio.file.Path;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntityType;

public abstract class AccessHelper {
    public static final AccessHelper INSTANCE = Services.load(AccessHelper.class);

    public abstract Path getModsDir();

    public abstract void populateBECountMap(Map<BlockEntityType<?>, Integer> map, Minecraft mc);
}
