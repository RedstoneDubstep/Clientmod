package net.redstonedubstep.clientmod.mixin.reloading;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nullable;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.misc.FieldHolder;

@Mixin(Minecraft.class)
public class MixinMinecraft {
	@Shadow
	private Overlay overlay;
	@Shadow
	@Nullable
	public LocalPlayer player;

	//At this point, reloading is fully done (and thus, the overlay gets removed so the player can move again), so we can do some post-stuff
	@Inject(method = "setOverlay", at = @At("HEAD"))
	private void onSetOverlay(Overlay newOverlay, CallbackInfo callbackInfo) {
		if (newOverlay == null && overlay != null) {
			if (ClientSettings.CONFIG.enhancedReloadingInfo.get()) {
				FieldHolder.currentTask = null;
				FieldHolder.maxTaskAmount = -1;

				if (Minecraft.getInstance().player != null && FieldHolder.reloadingStartTime >= 0) {
					long reloadDuration = FieldHolder.reloadingFinishTime - FieldHolder.reloadingStartTime;
					long totalDuration = System.currentTimeMillis() - FieldHolder.reloadingStartTime;

					if (reloadDuration >= 0 && totalDuration >= 0)
						Minecraft.getInstance().player.sendSystemMessage(Component.translatable("messages.clientmod:reloading.time", DurationFormatUtils.formatDuration(reloadDuration, "mm:ss.SSS"), DurationFormatUtils.formatDuration(totalDuration, "mm:ss.SSS")));
				}

				FieldHolder.reloadingStartTime = -1;
				FieldHolder.reloadingFinishTime = -1;
			}
		}
	}

	//Prevent reloading of server resources on join, so they only apply once the player reloads their resources willingly
	@Inject(method = "delayTextureReload", at = @At(value = "HEAD"), cancellable = true)
	private void onServerTextureReload(CallbackInfoReturnable<CompletableFuture<Void>> callback) {
		if (!ClientSettings.CONFIG.reloadServerResources.get() && player != null) {
			player.sendSystemMessage(Component.translatable("messages.clientmod:reloading.server_resources"));
			callback.setReturnValue(new CompletableFuture<>());
		}
	}
}
