package net.redstonedubstep.clientmod.mixin;

import javax.annotation.Nullable;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.network.chat.TranslatableComponent;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.misc.FieldHolder;

@Mixin(Minecraft.class)
public class MixinMinecraft {
	@Shadow @Nullable private Overlay overlay;

	//At this point, reloading is fully done (and thus, the overlay gets removed so the player can move again), so we can do some post-stuff
	@Inject(method = "setOverlay", at = @At("HEAD"))
	private void onSetOverlay(Overlay pLoadingGui, CallbackInfo callbackInfo) {
		if (pLoadingGui == null && overlay != null) {
			if (ClientSettings.CONFIG.enhancedReloadingInfo.get()) {
				FieldHolder.currentTask = null;
				FieldHolder.maxTaskAmount = -1;

				if (Minecraft.getInstance().player != null && FieldHolder.reloadingStartTime >= 0) {
					long reloadDuration = FieldHolder.reloadingFinishTime - FieldHolder.reloadingStartTime;
					long totalDuration = System.currentTimeMillis() - FieldHolder.reloadingStartTime;

					if (reloadDuration >= 0 && totalDuration >= 0)
						Minecraft.getInstance().player.sendMessage(new TranslatableComponent("messages.clientmod:reloading.time", DurationFormatUtils.formatDuration(reloadDuration, "mm:ss.SSS"), DurationFormatUtils.formatDuration(totalDuration, "mm:ss.SSS")), Util.NIL_UUID);
				}

				FieldHolder.reloadingStartTime = -1;
				FieldHolder.reloadingFinishTime = -1;
			}
		}
	}
}
