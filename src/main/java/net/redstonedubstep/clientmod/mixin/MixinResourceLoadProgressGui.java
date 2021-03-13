package net.redstonedubstep.clientmod.mixin;

import java.util.ArrayList;
import java.util.Set;

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
import net.redstonedubstep.clientmod.misc.MixinFieldStorage;

@Mixin(ResourceLoadProgressGui.class)
public class MixinResourceLoadProgressGui {
	@Final
	@Shadow
	private IAsyncReloader asyncReloader;

	//Adds a text to the resourceLoadProgressGui displaying the current task that's being done
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/ClientModLoader;renderProgressText()V"))
	private void injectRender(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (asyncReloader instanceof AsyncReloader) {
			Set<IFutureReloadListener> taskSet = ((AsyncReloader) asyncReloader).taskSet;

			if (MixinFieldStorage.oldTaskSet == null) {
				MixinFieldStorage.oldTaskSet = new ArrayList<>(taskSet);
			}

			if (!taskSet.equals(MixinFieldStorage.oldTaskSet)) { //update the current task
				for (IFutureReloadListener task : taskSet) {
					MixinFieldStorage.oldTaskSet.remove(task);
				}

				if (MixinFieldStorage.oldTaskSet.size() > 0) {
					MixinFieldStorage.currentTask = new ArrayList<>(MixinFieldStorage.oldTaskSet).get(0);
				}

				MixinFieldStorage.oldTaskSet = new ArrayList<>(taskSet);
			}

			if (MixinFieldStorage.currentTask != null)
				Minecraft.getInstance().fontRenderer.drawText(matrixStack, new StringTextComponent("Current Task: " + MixinFieldStorage.currentTask.getSimpleName()), 10, 20, 0);
		}
	}

}
