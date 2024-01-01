package redstonedubstep.mods.clientmod.mixin.reloading;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.SimpleReloadInstance;
import redstonedubstep.mods.clientmod.misc.FieldHolder;
import redstonedubstep.mods.clientmod.mixin.accessor.SimpleReloadInstanceAccessor;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

@Mixin(LoadingOverlay.class)
public abstract class LoadingOverlayMixin extends Overlay {
	@Shadow
	@Final
	private ReloadInstance reload;
	@Shadow
	@Final
	private boolean fadeIn;

	//Adds a text to the resourceLoadProgressGui displaying the current task that's being done
	@SuppressWarnings("unchecked")
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ReloadInstance;getActualProgress()F"))
	private void clientmod$injectRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (!FieldHolder.isMinecraftStarting && ClientSettings.INSTANCE.enhancedReloadingInfo() && Minecraft.getInstance().player != null) {
			if (fadeIn && reload instanceof SimpleReloadInstance && !reload.isDone()) {
				List<PreparableReloadListener> taskSet = new ArrayList<>(((SimpleReloadInstanceAccessor) reload).getPreparingListeners());

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
					graphics.drawString(Minecraft.getInstance().font, Component.literal("Current task: " + FieldHolder.currentTask.getName() + " (" + (FieldHolder.maxTaskAmount - taskSet.size()) + "/" + FieldHolder.maxTaskAmount + ")"), 10, 20, 0xFFFFFF);
			}
		}
	}

	//Toggle the Gui's background
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(Lnet/minecraft/client/renderer/RenderType;IIIII)V"))
	public void clientmod$redirectFill(GuiGraphics instance, RenderType type, int minX, int minY, int maxX, int maxY, int color) {
		instance.fill(type, minX, minY, maxX, maxY, fadeIn && !ClientSettings.INSTANCE.drawReloadingBackground() ? 16777215 : color);
	}

	//When reloading is finished, allows skipping to fade the overlay out and removes it instead
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J", ordinal = 1))
	public void clientmod$onReloadFinished(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (reload.isDone() && !ClientSettings.INSTANCE.reloadFade())
			Minecraft.getInstance().setOverlay(null);
	}

	//Fixes that the overlay cannot finish reloading as long as it hasn't faded in properly
	@Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;fadeIn:Z", opcode = 180, ordinal = 2)) //Opcodes.GETFIELD
	public boolean clientmod$shouldFadeIn(LoadingOverlay instance) {
		return fadeIn && ClientSettings.INSTANCE.reloadFade();
	}
}
