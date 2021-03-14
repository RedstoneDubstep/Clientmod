package net.redstonedubstep.clientmod.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ResourceLoadProgressGui;
import net.minecraft.resources.AsyncReloader;
import net.minecraft.resources.IAsyncReloader;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.util.text.StringTextComponent;
import net.redstonedubstep.clientmod.misc.FieldHolder;

@Mixin(ResourceLoadProgressGui.class)
public class MixinResourceLoadProgressGui {
	@Shadow @Final private IAsyncReloader asyncReloader;
	@Shadow @Final private boolean reloading;

	//Adds a text to the resourceLoadProgressGui displaying the current task that's being done
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/IAsyncReloader;estimateExecutionSpeed()F"))
	private void injectRender(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (reloading && asyncReloader instanceof AsyncReloader) {
			List<IFutureReloadListener> taskSet = new ArrayList<IFutureReloadListener>(((AsyncReloader) asyncReloader).taskSet);

			if (FieldHolder.oldTaskSet == null) {
				FieldHolder.oldTaskSet = new ArrayList<>(taskSet);
			}

			if (!taskSet.equals(FieldHolder.oldTaskSet)) { //update the current task
				for (IFutureReloadListener task : taskSet) {
					FieldHolder.oldTaskSet.remove(task);
				}

				if (FieldHolder.oldTaskSet.size() > 0) {
					FieldHolder.currentTask = new ArrayList<>(FieldHolder.oldTaskSet).get(0);
				}

				FieldHolder.oldTaskSet = new ArrayList<>(taskSet);
			}

			if (FieldHolder.currentTask != null)
				Minecraft.getInstance().fontRenderer.drawText(matrixStack, new StringTextComponent("Current task: " + FieldHolder.currentTask.getSimpleName()), 10, 20, 0);
		}

		if (asyncReloader.fullyDone()) {
			FieldHolder.currentTask = null;
		}
	}

}
