package redstonedubstep.mods.clientmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.network.chat.MutableComponent;
import redstonedubstep.mods.clientmod.platform.ClientSettings;
import redstonedubstep.mods.clientmod.command.CommandException;
import redstonedubstep.mods.clientmod.command.CommandLibrary;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
	//Automatically send chat messages as /teammsg commands
	@ModifyVariable(method = "handleChatInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getChat()Lnet/minecraft/client/gui/components/ChatComponent;"), argsOnly = true)
	private String clientmod$modifyChatInput(String original) {
		if (ClientSettings.INSTANCE.sendMessagesWithTeammsg() && !original.startsWith("/"))
			original = "/teammsg " + original;

		return original;
	}

	//Handle and parse chat inputs with the /clientmod prefix as commands from this mod
	@Inject(method = "handleChatInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getChat()Lnet/minecraft/client/gui/components/ChatComponent;"), cancellable = true)
	private void clientmod$onChatInput(String text, boolean addToChat, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (text.startsWith("/clientmod ")) { //if you for some reason can't use the mod's screen
			String command = text.replace("/clientmod ", "");
			CommandException result = CommandLibrary.parseAndExecuteCommand(command);

			if (result != null) {
				MutableComponent errorMessage = Component.translatable("command.failed");

				errorMessage.withStyle(s -> s.withHoverEvent(new HoverEvent(Action.SHOW_TEXT, result.getFullDescription()))).withStyle(ChatFormatting.RED);
				Minecraft.getInstance().player.sendSystemMessage(errorMessage);
			}

			callbackInfo.setReturnValue(false);
		}
	}
}
