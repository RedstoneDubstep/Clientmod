package net.redstonedubstep.clientmod.mixin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.SimpleReloadInstance;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.misc.FieldHolder;

@Mixin(LoadingOverlay.class)
public abstract class MixinLoadingOverlay extends Overlay {
	@Shadow
	@Final
	private ReloadInstance reload;
	@Shadow @Final private boolean fadeIn;

	//Adds a text to the resourceLoadProgressGui displaying the current task that's being done
	@SuppressWarnings("unchecked")
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ReloadInstance;getActualProgress()F"))
	private void injectRender(PoseStack stack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (ClientSettings.CONFIG.enhancedReloadingInfo.get() && fadeIn && reload instanceof SimpleReloadInstance && !reload.isDone()) {
			List<PreparableReloadListener> taskSet = new ArrayList<>(((SimpleReloadInstance<Void>)reload).preparingListeners);

			//setup reloading-related fields
			if (FieldHolder.maxTaskAmount <= -1)
				FieldHolder.maxTaskAmount = taskSet.size();

			if (FieldHolder.reloadingStartTime == -1)
				FieldHolder.reloadingStartTime = System.currentTimeMillis();

			if (FieldHolder.oldTaskSet == null || FieldHolder.oldTaskSet.isEmpty()) {
				FieldHolder.oldTaskSet = new ArrayList<>(taskSet);
			}

			if (!taskSet.equals(FieldHolder.oldTaskSet)) { //update the current task by comparing the new and the old task set
				for (PreparableReloadListener task : taskSet) {
					FieldHolder.oldTaskSet.remove(task);
				}

				if (FieldHolder.oldTaskSet.size() > 0) {
					FieldHolder.currentTask = new ArrayList<>(FieldHolder.oldTaskSet).get(0);
				}

				FieldHolder.oldTaskSet = new ArrayList<>(taskSet);
			}

			if (FieldHolder.currentTask != null)
				Minecraft.getInstance().font.draw(stack, new TextComponent("Current task: " + FieldHolder.currentTask.getName() + " (" + (FieldHolder.maxTaskAmount - taskSet.size()) + "/" + FieldHolder.maxTaskAmount + ")"), 10, 20, 0);
		}
		else if (reload.isDone() && FieldHolder.reloadingFinishTime == -1) {
			FieldHolder.reloadingFinishTime = System.currentTimeMillis();
		}
	}

	//Toggle the Gui's background
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;fill(Lcom/mojang/blaze3d/vertex/PoseStack;IIIII)V"))
	public void redirectFill(PoseStack stack, int minX, int minY, int maxX, int maxY, int color) {
		fill(stack, minX, minY, maxX, maxY, fadeIn && !ClientSettings.CONFIG.drawReloadingBackground.get() ? 16777215 : color);
	}

	//At this point, reloading is fully done, so we can do some post-stuff
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setOverlay(Lnet/minecraft/client/gui/screens/Overlay;)V"))
	public void onCloseLoadingGui(PoseStack stack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (ClientSettings.CONFIG.enhancedReloadingInfo.get()) {
			FieldHolder.currentTask = null;
			FieldHolder.maxTaskAmount = -1;

			if (Minecraft.getInstance().player != null) {
				long duration = FieldHolder.reloadingFinishTime - FieldHolder.reloadingStartTime;

				if (duration >= 0) {
					Minecraft.getInstance().player.sendMessage(new TranslatableComponent("messages.clientmod:reloading.time", DurationFormatUtils.formatDuration(duration, "mm:ss.SSS")), Util.NIL_UUID);
				}
			}

			FieldHolder.reloadingStartTime = -1;
			FieldHolder.reloadingFinishTime = -1;
		}
	}
}
