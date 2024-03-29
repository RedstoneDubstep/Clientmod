package redstonedubstep.mods.clientmod.screen;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import redstonedubstep.mods.clientmod.command.CommandException;
import redstonedubstep.mods.clientmod.command.CommandLibrary;

public class MainScreen extends Screen {
	EditBox inputField;
	MutableComponent helpMessage = Component.translatable("");
	List<MutableComponent> helpDescription = new ArrayList<>();

	public MainScreen() {
		super(Component.literal("screen.clientmod:mainScreen.name"));
	}

	@Override
	public void init() {
		super.init();

		inputField = new EditBox(font, width / 2 - 70, height / 2 - 10, 140, 20, CommonComponents.EMPTY);
		inputField.setTextColor(-1);
		inputField.setTextColorUneditable(-1);
		inputField.setBordered(true);
		inputField.setFocused(true);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		super.render(graphics, mouseX, mouseY, partialTicks);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		inputField.render(graphics, mouseX, mouseY, partialTicks);
		graphics.drawString(font, helpMessage, (width - font.width(helpMessage.getString())) / 2, (height + 30) / 2, 16711680);
		for (int i = 0; i < helpDescription.size(); i++) {
			graphics.drawString(font, helpDescription.get(i), (width - font.width(helpDescription.get(i).getString())) / 2, (height + 50 + 20 * i) / 2, 16711680);
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (inputField.isFocused()) {

			if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
				if (inputField.getValue().isEmpty())
					return false;
			}

			if (minecraft.options.keyInventory.matches(keyCode, scanCode))
				return false;
			else if (keyCode == GLFW.GLFW_KEY_ESCAPE)
				return super.keyPressed(keyCode, scanCode, modifiers);
			else if (keyCode == GLFW.GLFW_KEY_ENTER)
				return submitText(inputField.getValue());
			else if (keyCode == GLFW.GLFW_KEY_UP)
				inputField.setValue(CommandLibrary.lastInputText);
			else if (keyCode == GLFW.GLFW_KEY_DOWN)
				inputField.setValue("");
			else if (keyCode == GLFW.GLFW_KEY_TAB)
				inputField.setValue(CommandLibrary.getCompleteCommand(inputField.getValue()));
			else
				return inputField.keyPressed(keyCode, scanCode, modifiers);
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char typedChar, int keyCode) {
		if (inputField.isFocused()) {
			inputField.charTyped(typedChar, keyCode);
			return true;
		}
		else
			return super.charTyped(typedChar, keyCode);
	}

	private boolean submitText(String input) {
		if (input.isEmpty())
			setHelpMessages(CommandException.empty());
		else {
			CommandException result = CommandLibrary.parseAndExecuteCommand(input);
			setHelpMessages(result);
		}

		return true;
	}

	private void setHelpMessages(CommandException result) {
		if (!inputField.getValue().isEmpty())
			CommandLibrary.lastInputText = inputField.getValue();

		if (result != null) {
			helpMessage = result.getTitle();
			helpDescription = result.getDescription();
			inputField.setValue("");
		}
		else if (minecraft.screen.equals(this)) {
			resetHelpMessages();
			onClose();
		}
	}

	private void resetHelpMessages() {
		helpMessage = Component.translatable("");
		helpDescription = new ArrayList<>();
	}

	@Override
	public void removed() {
		super.removed();
	}
}
