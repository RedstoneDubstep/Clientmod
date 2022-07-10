package net.redstonedubstep.clientmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.network.chat.MutableComponent;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.command.CommandException;
import net.redstonedubstep.clientmod.command.CommandLibrary;

@Mixin(ChatScreen.class)
public class MixinChatScreen {
	@ModifyVariable(method = "handleChatInput", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/ChatScreen;chatPreview:Lnet/minecraft/client/gui/chat/ClientChatPreview;"), argsOnly = true)
	private String modifyChatInput(String original) {
		if (ClientSettings.SEND_MESSAGES_WITH_TEAMMSG.get() && !original.startsWith("/"))
			original = "/teammsg " + original;

		return original;
	}

	@Inject(method = "handleChatInput", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/ChatScreen;chatPreview:Lnet/minecraft/client/gui/chat/ClientChatPreview;"), cancellable = true)
	private void onChatInput(String text, boolean addToChat, CallbackInfo callbackInfo) {
		if (text.startsWith("/clientmod ")) { //if you for some reason can't use the mod's screen
			String command = text.replace("/clientmod ", "");
			CommandException result = CommandLibrary.parseAndExecuteCommand(command);

			if (result != null) {
				MutableComponent errorMessage = Component.translatable("command.failed");

				errorMessage.withStyle(s -> s.withHoverEvent(new HoverEvent(Action.SHOW_TEXT, result.getFullDescription()))).withStyle(ChatFormatting.RED);
				Minecraft.getInstance().player.sendSystemMessage(errorMessage);
			}

			callbackInfo.cancel();
		}
	}
}
