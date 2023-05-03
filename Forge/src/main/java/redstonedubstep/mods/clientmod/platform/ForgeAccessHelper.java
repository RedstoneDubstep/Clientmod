package redstonedubstep.mods.clientmod.platform;

import java.nio.file.Path;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.loading.FMLPaths;

public class ForgeAccessHelper extends AccessHelper {
    @Override
    public Path getModsDir() {
        return FMLPaths.MODSDIR.get();
    }

    @Override
    public void populateBECountMap(Map<BlockEntityType<?>, Integer> map, Minecraft mc) {
        for (LevelRenderer.RenderChunkInfo chunkInfo : mc.levelRenderer.renderChunksInFrustum) {
            for (BlockEntity be : chunkInfo.chunk.getCompiledChunk().getRenderableBlockEntities()) {
                if (mc.levelRenderer.cullingFrustum.isVisible(be.getRenderBoundingBox())) {
                    map.computeIfPresent(be.getType(), (t, i) -> i + 1);
                    map.putIfAbsent(be.getType(), 1);
                }
            }
        }

        synchronized (mc.levelRenderer.globalBlockEntities) {
            for (BlockEntity be : mc.levelRenderer.globalBlockEntities) {
                if (mc.levelRenderer.cullingFrustum.isVisible(be.getRenderBoundingBox())) {
                    map.computeIfPresent(be.getType(), (t, i) -> i + 1);
                    map.putIfAbsent(be.getType(), 1);
                }
            }
        }
    }
}
