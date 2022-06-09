package net.redstonedubstep.clientmod.mixin;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Opcodes;
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
import net.minecraft.network.chat.Component;
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
		if (ClientSettings.CONFIG.enhancedReloadingInfo.get() && fadeIn && reload instanceof SimpleReloadInstance && Minecraft.getInstance().player != null) {
			if (!reload.isDone()) {
				List<PreparableReloadListener> taskSet = new ArrayList<>(((SimpleReloadInstance<Void>)reload).preparingListeners);

				//setup reloading-related fields
				if (FieldHolder.maxTaskAmount == -1)
					FieldHolder.maxTaskAmount = taskSet.size();

				if (FieldHolder.oldTaskSet == null || FieldHolder.oldTaskSet.isEmpty())
					FieldHolder.oldTaskSet = new ArrayList<>(taskSet);

				if (!taskSet.equals(FieldHolder.oldTaskSet)) { //update the current task by comparing the new and the old task set
					for (PreparableReloadListener task : taskSet) {
						FieldHolder.oldTaskSet.remove(task);
					}

					if (FieldHolder.oldTaskSet.size() > 0)
						FieldHolder.currentTask = new ArrayList<>(FieldHolder.oldTaskSet).get(0);

					FieldHolder.oldTaskSet = new ArrayList<>(taskSet);
				}

				if (FieldHolder.currentTask != null)
					Minecraft.getInstance().font.draw(stack, Component.literal("Current task: " + FieldHolder.currentTask.getName() + " (" + (FieldHolder.maxTaskAmount - taskSet.size()) + "/" + FieldHolder.maxTaskAmount + ")"), 10, 20, 0);
			}
			else if (FieldHolder.reloadingFinishTime == -1) {
				FieldHolder.reloadingFinishTime = System.currentTimeMillis();
				Minecraft.getInstance().player.sendMessage(Component.translatable("messages.clientmod:reloading.finished"), Util.NIL_UUID);
			}
		}
	}

	//Toggle the Gui's background
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;fill(Lcom/mojang/blaze3d/vertex/PoseStack;IIIII)V"))
	public void redirectFill(PoseStack stack, int minX, int minY, int maxX, int maxY, int color) {
		fill(stack, minX, minY, maxX, maxY, fadeIn && !ClientSettings.CONFIG.drawReloadingBackground.get() ? 16777215 : color);
	}

	//When reloading is finished, allows skipping to fade the overlay out and removes it instead
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J", ordinal = 1))
	public void onReloadFinished(PoseStack stack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (reload.isDone() && !ClientSettings.CONFIG.reloadFade.get())
			Minecraft.getInstance().setOverlay(null);
	}

	//Fixes that the overlay cannot finish reloading as long as it hasn't faded in properly
	@Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;fadeIn:Z", opcode = Opcodes.GETFIELD, ordinal = 2))
	public boolean shouldFadeIn(LoadingOverlay instance) {
		return fadeIn && ClientSettings.CONFIG.reloadFade.get();
	}
}
