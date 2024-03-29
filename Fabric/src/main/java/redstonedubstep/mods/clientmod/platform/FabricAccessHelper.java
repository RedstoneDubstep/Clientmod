package redstonedubstep.mods.clientmod.platform;

import java.nio.file.Path;
import java.util.Map;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FabricAccessHelper extends AccessHelper {
    @Override
    public Path getModsDir() {
        return FabricLoader.getInstance().getGameDir().resolve("mods");
    }

    @Override
    public void populateBECountMap(Map<BlockEntityType<?>, Integer> map, Minecraft mc) {
        for (SectionRenderDispatcher.RenderSection section : mc.levelRenderer.visibleSections) {
            for (BlockEntity be : section.getCompiled().getRenderableBlockEntities()) {
                map.computeIfPresent(be.getType(), (t, i) -> i + 1);
                map.putIfAbsent(be.getType(), 1);
            }
        }

        synchronized (mc.levelRenderer.globalBlockEntities) {
            for (BlockEntity be : mc.levelRenderer.globalBlockEntities) {
                map.computeIfPresent(be.getType(), (t, i) -> i + 1);
                map.putIfAbsent(be.getType(), 1);
            }
        }
    }
}
