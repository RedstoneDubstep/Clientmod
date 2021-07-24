package net.redstonedubstep.clientmod.mixin;

import java.util.ArrayList;
import java.util.List;

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
import net.minecraft.util.text.StringTextComponent;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.misc.FieldHolder;

@Mixin(ResourceLoadProgressGui.class)
public abstract class MixinResourceLoadProgressGui extends LoadingGui {
	@Shadow @Final private IAsyncReloader asyncReloader;
	@Shadow @Final private boolean reloading;

	//Adds a text to the resourceLoadProgressGui displaying the current task that's being done
	@SuppressWarnings("unchecked")
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/IAsyncReloader;estimateExecutionSpeed()F"))
	private void injectRender(MatrixStack stack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (reloading && asyncReloader instanceof AsyncReloader && !asyncReloader.fullyDone()) {
			List<IFutureReloadListener> taskSet = new ArrayList<>(((AsyncReloader<Void>)asyncReloader).taskSet);

			//setup reloading-related fields
			if (FieldHolder.maxTaskAmount <= -1)
				FieldHolder.maxTaskAmount = taskSet.size();

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
				Minecraft.getInstance().fontRenderer.drawText(stack, new StringTextComponent("Current task: " + FieldHolder.currentTask.getSimpleName() + " (" + (FieldHolder.maxTaskAmount - taskSet.size()) + "/" + FieldHolder.maxTaskAmount + ")"), 10, 20, 0);
		}

		if (asyncReloader.fullyDone()) {
			FieldHolder.currentTask = null;
		}
	}

	//Toggle the Gui's background
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/ResourceLoadProgressGui;fill(Lcom/mojang/blaze3d/matrix/MatrixStack;IIIII)V"))
	public void redirectFill(MatrixStack matrix, int minX, int minY, int maxX, int maxY, int color) {
		fill(matrix, minX, minY, maxX, maxY, reloading && !ClientSettings.CONFIG.drawReloadingBackground.get() ? 16777215 : color);
	}
}
