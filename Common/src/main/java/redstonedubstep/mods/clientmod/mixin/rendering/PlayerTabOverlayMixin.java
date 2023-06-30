package redstonedubstep.mods.clientmod.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
	@Inject(method = "renderPingIcon", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"))
	private void clientmod$onRenderPingIcon(GuiGraphics guiGraphics, int width, int x, int y, PlayerInfo info, CallbackInfo callbackInfo) {
		if (ClientSettings.INSTANCE.showPingData())
			guiGraphics.drawString(Minecraft.getInstance().font, String.valueOf(info.getLatency()), x + width - 11, y, 0xD0D0D0, false);
	}
}
