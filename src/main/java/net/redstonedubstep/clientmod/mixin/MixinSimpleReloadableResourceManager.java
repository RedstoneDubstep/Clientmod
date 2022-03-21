package net.redstonedubstep.clientmod.mixin;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.fonts.FontResourceManager;
import net.minecraft.resources.AsyncReloader;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.Unit;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.misc.FieldHolder;

@Mixin(SimpleReloadableResourceManager.class)
public abstract class MixinSimpleReloadableResourceManager {
	//stop sounds from reloading, and fill some fields with information about the start of the reload
	@Redirect(method = "createReload", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/AsyncReloader;of(Lnet/minecraft/resources/IResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;)Lnet/minecraft/resources/AsyncReloader;"))
	public AsyncReloader<Void> onInitializeAsyncReloader(IResourceManager resourceManager, List<IFutureReloadListener> listeners, Executor backgroundExecutor, Executor gameExecutor, CompletableFuture<Unit> waitingFor) {
		if (Minecraft.getInstance().level != null) { //makes sure this only applies when reloading resources ingame, and not when starting the game
			if (FieldHolder.reloadFilter != null) {
				listeners = listeners.stream().filter(l -> FieldHolder.reloadFilter.stream().anyMatch(c -> c.isInstance(l) || (c == FontResourceManager.class && l.getName().equals("FontManager")))).collect(Collectors.toList());
				FieldHolder.reloadFilter = null;
			}

			if (ClientSettings.CONFIG.enhancedReloadingInfo.get()) {
				FieldHolder.maxTaskAmount = listeners.size();
				FieldHolder.oldTaskSet = listeners;
			}

			if (FieldHolder.reloadingStartTime == -1)
				FieldHolder.reloadingStartTime = System.currentTimeMillis();
		}
		
		return AsyncReloader.of(resourceManager, listeners, backgroundExecutor, gameExecutor, waitingFor);
	}
}
