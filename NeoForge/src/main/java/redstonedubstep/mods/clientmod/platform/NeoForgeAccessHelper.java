package redstonedubstep.mods.clientmod.platform;

import java.nio.file.Path;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.loading.FMLPaths;

public class NeoForgeAccessHelper extends AccessHelper {
    @Override
    public Path getModsDir() {
        return FMLPaths.MODSDIR.get();
    }

    @Override
    public void populateBECountMap(Map<BlockEntityType<?>, Integer> map, Minecraft mc) {
        for (SectionRenderDispatcher.RenderSection section : mc.levelRenderer.visibleSections) {
            for (BlockEntity be : section.getCompiled().getRenderableBlockEntities()) {
                BlockEntityRenderer<BlockEntity> renderer = mc.getBlockEntityRenderDispatcher().getRenderer(be);

                if (renderer != null && mc.levelRenderer.cullingFrustum.isVisible(renderer.getRenderBoundingBox(be))) {
                    map.computeIfPresent(be.getType(), (t, i) -> i + 1);
                    map.putIfAbsent(be.getType(), 1);
                }
            }
        }

        synchronized (mc.levelRenderer.globalBlockEntities) {
            for (BlockEntity be : mc.levelRenderer.globalBlockEntities) {
                BlockEntityRenderer<BlockEntity> renderer = mc.getBlockEntityRenderDispatcher().getRenderer(be);

                if (renderer != null && mc.levelRenderer.cullingFrustum.isVisible(renderer.getRenderBoundingBox(be))) {
                    map.computeIfPresent(be.getType(), (t, i) -> i + 1);
                    map.putIfAbsent(be.getType(), 1);
                }
            }
        }
    }
}
