package net.redstonedubstep.clientmod.mixin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.AsyncReloader;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IFutureReloadListener.IStage;
import net.minecraft.resources.IResourceManager;
import net.redstonedubstep.clientmod.ClientSettings;

@Mixin(AsyncReloader.class)
public class MixinAsyncReloader {
	@Redirect(method = "lambda$of$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/IFutureReloadListener;reload(Lnet/minecraft/resources/IFutureReloadListener$IStage;Lnet/minecraft/resources/IResourceManager;Lnet/minecraft/profiler/IProfiler;Lnet/minecraft/profiler/IProfiler;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
	private static CompletableFuture<Void> redirectReload(IFutureReloadListener instance, IStage preparationBarrier, IResourceManager resourceManager, IProfiler profilerFiller, IProfiler secondProfileFiller, Executor backgroundExecutor, Executor gameExecutor, Executor unusedBackgroundExecutor, IStage unusedBarrier, IResourceManager unusedResourceManager, IFutureReloadListener unusedReloadListener, Executor correctBackgroundExecutor) {
		if (ClientSettings.CONFIG.enhancedReloadingInfo.get())
			backgroundExecutor = correctBackgroundExecutor;

		return instance.reload(preparationBarrier, resourceManager, profilerFiller, secondProfileFiller, backgroundExecutor, gameExecutor);
	}
}
