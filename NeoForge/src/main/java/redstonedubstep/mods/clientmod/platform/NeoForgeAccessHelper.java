package redstonedubstep.mods.clientmod.platform;

import java.nio.file.Path;
import java.util.Map;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.loading.FMLPaths;

public class NeoForgeAccessHelper extends AccessHelper {
    @Override
    public Path getModsDir() {
        return FMLPaths.MODSDIR.get();
    }

    @Override
    public void populateBECountMap(Map<BlockEntityType<?>, Integer> map, Minecraft mc) {
		Camera camera = mc.gameRenderer.getMainCamera();
		Frustum cullingFrustum = mc.gameRenderer.getMainCamera().getCullFrustum();
		Vec3 camPos = camera.position();

		cullingFrustum.prepare(camPos.x, camPos.y, camPos.z);

        for (SectionRenderDispatcher.RenderSection section : mc.levelRenderer.visibleSections) {
            for (BlockEntity be : section.getSectionMesh().getRenderableBlockEntities()) {
                BlockEntityRenderer<BlockEntity, BlockEntityRenderState> renderer = mc.getBlockEntityRenderDispatcher().getRenderer(be);

                if (renderer != null && cullingFrustum.isVisible(renderer.getRenderBoundingBox(be))) {
                    map.computeIfPresent(be.getType(), (t, i) -> i + 1);
                    map.putIfAbsent(be.getType(), 1);
                }
            }
        }

        synchronized (mc.level.getGloballyRenderedBlockEntities()) {
            for (BlockEntity be : mc.level.getGloballyRenderedBlockEntities()) {
                BlockEntityRenderer<BlockEntity, BlockEntityRenderState> renderer = mc.getBlockEntityRenderDispatcher().getRenderer(be);

                if (renderer != null && cullingFrustum.isVisible(renderer.getRenderBoundingBox(be))) {
                    map.computeIfPresent(be.getType(), (t, i) -> i + 1);
                    map.putIfAbsent(be.getType(), 1);
                }
            }
        }
    }
}
