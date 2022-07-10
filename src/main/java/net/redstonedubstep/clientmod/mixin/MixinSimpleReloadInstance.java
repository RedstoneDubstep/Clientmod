package net.redstonedubstep.clientmod.mixin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.PreparableReloadListener.PreparationBarrier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleReloadInstance;
import net.minecraft.util.profiling.ProfilerFiller;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.misc.FieldHolder;

@Mixin(SimpleReloadInstance.class)
public class MixinSimpleReloadInstance {
	@Redirect(method = "lambda$of$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/PreparableReloadListener;reload(Lnet/minecraft/server/packs/resources/PreparableReloadListener$PreparationBarrier;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;Lnet/minecraft/util/profiling/ProfilerFiller;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
	private static CompletableFuture<Void> redirectReload(PreparableReloadListener instance, PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller secondProfileFiller, Executor backgroundExecutor, Executor gameExecutor, Executor unusedBackgroundExecutor, PreparationBarrier unusedBarrier, ResourceManager unusedResourceManager, PreparableReloadListener unusedReloadListener, Executor correctBackgroundExecutor) {
		if (!FieldHolder.isMinecraftStarting && ClientSettings.CONFIG.enhancedReloadingInfo.get())
			backgroundExecutor = correctBackgroundExecutor;

		return instance.reload(preparationBarrier, resourceManager, profilerFiller, secondProfileFiller, backgroundExecutor, gameExecutor);
	}
}
