package net.redstonedubstep.clientmod.mixin;

import net.minecraft.client.gui.screens.Overlay;

//@Mixin(LoadingOverlay.class)
public abstract class MixinResourceLoadProgressGui extends Overlay {
	/*@Shadow @Final private ReloadInstance asyncReloader;
	@Shadow @Final private boolean reloading;

	//Adds a text to the resourceLoadProgressGui displaying the current task that's being done
	@SuppressWarnings("unchecked")
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/IAsyncReloader;estimateExecutionSpeed()F"))
	private void injectRender(PoseStack stack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (ClientSettings.CONFIG.enhancedReloadingInfo.get() && reloading && asyncReloader instanceof SimpleReloadInstance && !asyncReloader.isDone()) {
			List<PreparableReloadListener> taskSet = new ArrayList<>(((SimpleReloadInstance<Void>)asyncReloader).preparingListeners);

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
		else if (asyncReloader.isDone() && FieldHolder.reloadingFinishTime == -1) {
			FieldHolder.reloadingFinishTime = System.currentTimeMillis();
		}
	}

	//Toggle the Gui's background
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/ResourceLoadProgressGui;fill(Lcom/mojang/blaze3d/matrix/MatrixStack;IIIII)V"))
	public void redirectFill(PoseStack stack, int minX, int minY, int maxX, int maxY, int color) {
		fill(stack, minX, minY, maxX, maxY, reloading && !ClientSettings.CONFIG.drawReloadingBackground.get() ? 16777215 : color);
	}

	//At this point, reloading is fully done, so we can do some post-stuff
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setLoadingGui(Lnet/minecraft/client/gui/LoadingGui;)V"))
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
	}*/
}
