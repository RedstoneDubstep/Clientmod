package net.redstonedubstep.clientmod.mixin;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleReloadInstance;
import net.minecraft.server.packs.resources.SimpleReloadableResourceManager;
import net.minecraft.util.Unit;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.misc.FieldHolder;

@Mixin(SimpleReloadableResourceManager.class)
public abstract class MixinSimpleReloadableResourceManager {
	//stop sounds from reloading, and fill some fields with information about the start of the reload
	@Redirect(method = "createReload", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/SimpleReloadInstance;of(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;)Lnet/minecraft/server/packs/resources/SimpleReloadInstance;"))
	public SimpleReloadInstance<Void> onNewSimpleReloadInstance(ResourceManager resourceManager, List<PreparableReloadListener> listeners, Executor backgroundExecutor, Executor gameExecutor, CompletableFuture<Unit> waitingFor) {
		if (Minecraft.getInstance().level != null) { //makes sure this only applies when reloading resources ingame, and not when starting the game
			if (FieldHolder.reloadFilter != null) {
				listeners = listeners.stream().filter(l -> FieldHolder.reloadFilter.stream().anyMatch(c -> c.isInstance(l) || (c == FontManager.class && l.getName().equals("FontManager")))).collect(Collectors.toList()); //TODO: implementation of that idea (writing it down because it's way to late to remember stuff): reload command should set the filter in FieldHolder and then trigger a normal reload via Minecraft.getInstance(), this mixin intercepts that and modifies the list according to the filter before resetting the filter
				FieldHolder.reloadFilter = null;
			}

			if (ClientSettings.CONFIG.enhancedReloadingInfo.get()) {
				FieldHolder.maxTaskAmount = listeners.size();
				FieldHolder.oldTaskSet = listeners;
			}
		}
		
		return SimpleReloadInstance.of(resourceManager, listeners, backgroundExecutor, gameExecutor, waitingFor);
	}
}
