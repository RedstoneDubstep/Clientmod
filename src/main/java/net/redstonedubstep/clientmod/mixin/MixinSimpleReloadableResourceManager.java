package net.redstonedubstep.clientmod.mixin;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.resources.IAsyncReloader;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.Unit;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.misc.FieldHolder;

@Mixin(SimpleReloadableResourceManager.class)
public abstract class MixinSimpleReloadableResourceManager {
	//stop sounds from reloading, and fill some fields with information about the start of the reload
	@Redirect(method = "reloadResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/SimpleReloadableResourceManager;initializeAsyncReloader(Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/List;Ljava/util/concurrent/CompletableFuture;)Lnet/minecraft/resources/IAsyncReloader;"))
	public IAsyncReloader onInitializeAsyncReloader(SimpleReloadableResourceManager simpleReloadableResourceManager, Executor backgroundExecutor, Executor gameExecutor, List<IFutureReloadListener> listeners, CompletableFuture<Unit> waitingFor) {
		if (Minecraft.getInstance().world != null && !ClientSettings.CONFIG.shouldReloadSounds.get()) {
			listeners = listeners.stream().filter(l -> !(l instanceof SoundHandler)).collect(Collectors.toList());
		}

		FieldHolder.maxTaskAmount = listeners.size();
		FieldHolder.oldTaskSet = listeners;
		return simpleReloadableResourceManager.initializeAsyncReloader(backgroundExecutor, gameExecutor, listeners, waitingFor);
	}
}
