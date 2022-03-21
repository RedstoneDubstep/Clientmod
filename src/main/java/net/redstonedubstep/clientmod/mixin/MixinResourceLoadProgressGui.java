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

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LoadingGui;
import net.minecraft.client.gui.ResourceLoadProgressGui;
import net.minecraft.resources.AsyncReloader;
import net.minecraft.resources.IAsyncReloader;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.misc.FieldHolder;

@Mixin(ResourceLoadProgressGui.class)
public abstract class MixinResourceLoadProgressGui extends LoadingGui {
	@Shadow @Final private IAsyncReloader reload;
	@Shadow @Final private boolean fadeIn;

	//Adds a text to the resourceLoadProgressGui displaying the current task that's being done
	@SuppressWarnings("unchecked")
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/IAsyncReloader;getActualProgress()F"))
	private void injectRender(MatrixStack stack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (ClientSettings.CONFIG.enhancedReloadingInfo.get() && fadeIn && reload instanceof AsyncReloader && !reload.isDone()) {
			List<IFutureReloadListener> taskSet = new ArrayList<>(((AsyncReloader<Void>)reload).preparingListeners);

			//setup reloading-related fields
			if (FieldHolder.maxTaskAmount <= -1)
				FieldHolder.maxTaskAmount = taskSet.size();

			if (FieldHolder.reloadingStartTime == -1)
				FieldHolder.reloadingStartTime = System.currentTimeMillis();

			if (FieldHolder.oldTaskSet == null || FieldHolder.oldTaskSet.isEmpty()) {
				FieldHolder.oldTaskSet = new ArrayList<>(taskSet);
			}

			if (!taskSet.equals(FieldHolder.oldTaskSet)) { //update the current task by comparing the new and the old task set
				for (IFutureReloadListener task : taskSet) {
					FieldHolder.oldTaskSet.remove(task);
				}

				if (FieldHolder.oldTaskSet.size() > 0) {
					FieldHolder.currentTask = new ArrayList<>(FieldHolder.oldTaskSet).get(0);
				}

				FieldHolder.oldTaskSet = new ArrayList<>(taskSet);
			}

			if (FieldHolder.currentTask != null)
				Minecraft.getInstance().font.draw(stack, new StringTextComponent("Current task: " + FieldHolder.currentTask.getName() + " (" + (FieldHolder.maxTaskAmount - taskSet.size()) + "/" + FieldHolder.maxTaskAmount + ")"), 10, 20, 0);
		}
		else if (reload.isDone() && FieldHolder.reloadingFinishTime == -1) {
			FieldHolder.reloadingFinishTime = System.currentTimeMillis();
		}
	}

	//Toggle the Gui's background
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/ResourceLoadProgressGui;fill(Lcom/mojang/blaze3d/matrix/MatrixStack;IIIII)V"))
	public void redirectFill(MatrixStack stack, int minX, int minY, int maxX, int maxY, int color) {
		fill(stack, minX, minY, maxX, maxY, fadeIn && !ClientSettings.CONFIG.drawReloadingBackground.get() ? 16777215 : color);
	}

	//At this point, reloading is fully done, so we can do some post-stuff
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setOverlay(Lnet/minecraft/client/gui/LoadingGui;)V"))
	public void onCloseLoadingGui(MatrixStack stack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (ClientSettings.CONFIG.enhancedReloadingInfo.get()) {
			FieldHolder.currentTask = null;
			FieldHolder.maxTaskAmount = -1;

			if (Minecraft.getInstance().player != null) {
				long reloadDuration = FieldHolder.reloadingFinishTime - FieldHolder.reloadingStartTime;
				long totalDuration = System.currentTimeMillis() - FieldHolder.reloadingStartTime;

				if (reloadDuration >= 0 && totalDuration >= 0) {
					Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("messages.clientmod:reloading.time", DurationFormatUtils.formatDuration(reloadDuration, "mm:ss.SSS"), DurationFormatUtils.formatDuration(totalDuration, "mm:ss.SSS")), Util.NIL_UUID);
				}
			}

			FieldHolder.reloadingStartTime = -1;
			FieldHolder.reloadingFinishTime = -1;
		}
	}
}
