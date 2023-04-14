package net.redstonedubstep.clientmod.mixin.reloading;

import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.misc.FieldHolder;

@Mixin(ReloadInstance.class)
public interface ReloadInstanceMixin {
	@Shadow
	CompletableFuture<?> done();

	/**
	 * Checks whether the current reload is done, and if so, logs a message containing the time it took. I hate using @Overwrite
	 * so much, but this seems to be the only option here.
	 *
	 * @author Redstone_Dubstep
	 * @reason You can't inject into interfaces, and I need to cover all uses of isDone in order to detect whether a reload is
	 * done
	 */
	@Overwrite
	default boolean isDone() {
		boolean isDone = done().isDone();

		if (isDone && !FieldHolder.wasReloadingDone && !FieldHolder.isMinecraftStarting && ClientSettings.CONFIG.enhancedReloadingInfo.get()) {
			if (FieldHolder.reloadingStartTime >= 0) {
				long duration = System.currentTimeMillis() - FieldHolder.reloadingStartTime;

				Minecraft.getInstance().player.sendSystemMessage(Component.translatable("messages.clientmod:reloading.finished", DurationFormatUtils.formatDuration(duration, "mm:ss.SSS")));

				if (FieldHolder.reloadingFinishTime == -1)
					FieldHolder.reloadingFinishTime = System.currentTimeMillis();
			}
		}

		FieldHolder.wasReloadingDone = isDone;
		return isDone;
	}
}
