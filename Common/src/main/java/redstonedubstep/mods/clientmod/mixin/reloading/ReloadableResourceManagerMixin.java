package redstonedubstep.mods.clientmod.mixin.reloading;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import redstonedubstep.mods.clientmod.misc.FieldHolder;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Mixin(ReloadableResourceManager.class)
public class ReloadableResourceManagerMixin {
	//Apply any set reload filter, and fill some fields with information about the start of the reload
	@Redirect(method = "createReload", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/SimpleReloadInstance;create(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/server/packs/resources/ReloadInstance;"))
	public ReloadInstance clientmod$onNewReloadInstance(ResourceManager resourceManager, List<PreparableReloadListener> listeners, Executor backgroundExecutor, Executor gameExecutor, CompletableFuture<Unit> waitingFor, boolean debugEnabled) {
		if (Minecraft.getInstance().player != null) { //makes sure this only applies when reloading resources ingame, and not when starting the game
			if (FieldHolder.reloadFilter != null) {
				boolean filterAllowsFontManager = FieldHolder.reloadFilter.test(FontManager.class);

				listeners = listeners.stream().filter(l -> l.getName().equals("FontManager") ? filterAllowsFontManager : FieldHolder.reloadFilter.test(l.getClass())).collect(Collectors.toList());
				FieldHolder.reloadFilter = null;
			}

			if (ClientSettings.INSTANCE.enhancedReloadingInfo()) {
				List<PreparableReloadListener> readOnlyListeners = listeners;

				FieldHolder.maxTaskAmount = listeners.size();
				FieldHolder.oldTaskSet = new ArrayList<>(listeners);
				FieldHolder.reloadingStartTime = System.currentTimeMillis();
				Minecraft.getInstance().player.sendSystemMessage(Component.translatable("messages.clientmod:reloading.started", listeners.size()).withStyle(s -> s.withHoverEvent(new HoverEvent(Action.SHOW_TEXT, ComponentUtils.formatList(readOnlyListeners.stream().map(PreparableReloadListener::getName).toList())))));
			}
		}

		return SimpleReloadInstance.create(resourceManager, listeners, backgroundExecutor, gameExecutor, waitingFor, debugEnabled);
	}
}
