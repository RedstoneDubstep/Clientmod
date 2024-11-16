package redstonedubstep.mods.clientmod.mixin.reloading;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.PreparableReloadListener.PreparationBarrier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleReloadInstance;
import redstonedubstep.mods.clientmod.misc.FieldHolder;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

@Mixin(SimpleReloadInstance.class)
public abstract class SimpleReloadInstanceMixin {
	//Fix incorrect background executor being parsed into the redirected method, for slightly more accurate loading progress bar
	@Redirect(method = "lambda$of$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/PreparableReloadListener;reload(Lnet/minecraft/server/packs/resources/PreparableReloadListener$PreparationBarrier;Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
	private static CompletableFuture<Void> clientmod$redirectReload(PreparableReloadListener instance, PreparationBarrier preparationBarrier, ResourceManager resourceManager, Executor backgroundExecutor, Executor gameExecutor, Executor unusedBackgroundExecutor, PreparationBarrier unusedBarrier, ResourceManager unusedResourceManager, PreparableReloadListener unusedReloadListener, Executor correctBackgroundExecutor) {
		if (!FieldHolder.isMinecraftStarting && ClientSettings.INSTANCE.enhancedReloadingInfo())
			backgroundExecutor = correctBackgroundExecutor;

		return instance.reload(preparationBarrier, resourceManager, backgroundExecutor, gameExecutor);
	}
}
