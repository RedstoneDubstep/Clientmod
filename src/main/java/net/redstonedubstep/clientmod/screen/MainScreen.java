package net.redstonedubstep.clientmod.screen;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.redstonedubstep.clientmod.command.CommandException;
import net.redstonedubstep.clientmod.command.CommandLibrary;

public class MainScreen extends Screen {

	TextFieldWidget inputField;
	ITextComponent helpMessage = new TranslationTextComponent("");
	List<ITextComponent> helpDescription = new ArrayList<>();

	public MainScreen() {
		super(new StringTextComponent("main_screen"));
	}

	@Override
	public void init() {
		super.init();

		minecraft.keyboardListener.enableRepeatEvents(true);
		inputField = new TextFieldWidget(font, width / 2 - 70, height / 2 - 10, 140, 20, StringTextComponent.EMPTY);
		inputField.setTextColor(-1);
		inputField.setDisabledTextColour(-1);
		inputField.setEnableBackgroundDrawing(true);
		inputField.setFocused2(true);

	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrix);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		inputField.render(matrix, mouseX, mouseY, partialTicks);
		font.drawText(matrix, helpMessage, (width - font.getStringWidth(helpMessage.getString())) / 2, (height + 30) / 2, 16711680);
		for (int i = 0; i < helpDescription.size(); i++) {
			font.drawText(matrix, helpDescription.get(i), (width - font.getStringWidth(helpMessage.getString())) / 2, (height + 50 + 20 * i) / 2, 16711680);
		}

		super.render(matrix, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (inputField.isFocused()) {
			resetHelpMessages();

			if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
				if (inputField.getText().isEmpty())
					return false;
			}

			if (keyCode == minecraft.gameSettings.keyBindInventory.getKey().getKeyCode())
				return false;
			else if (keyCode == GLFW.GLFW_KEY_ESCAPE)
				return super.keyPressed(keyCode, scanCode, modifiers);
			else if (keyCode == GLFW.GLFW_KEY_ENTER)
				return submitText(inputField.getText());
			else if (keyCode == GLFW.GLFW_KEY_UP)
				inputField.setText(CommandLibrary.lastInputText);
			else if (keyCode == GLFW.GLFW_KEY_DOWN)
				inputField.setText("");
			else if (keyCode == GLFW.GLFW_KEY_TAB)
				inputField.setText(CommandLibrary.getCompleteCommand(inputField.getText()));
			else
				return inputField.keyPressed(keyCode, scanCode, modifiers);
		}
		
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char typedChar, int keyCode) {
		if (inputField.isFocused()) {
			inputField.charTyped(typedChar, keyCode);
			resetHelpMessages();
			return true;
		}
		else
			return super.charTyped(typedChar, keyCode);
	}

	private boolean submitText(String input) {
		if (input.isEmpty())
			setHelpMessages(CommandException.empty());
		else {

			String prefix = input.split(" ")[0];
			String parameter = input.contains(" ") ? input.split(" ", 2)[1] : "";

			CommandException result = CommandLibrary.findAndExecuteCommand(prefix, parameter);
			setHelpMessages(result);
		}

		return true;
	}

	private void setHelpMessages(CommandException result) {
		CommandLibrary.lastInputText = inputField.getText();

		if (result != null) {
			helpMessage = result.getTitle();
			helpDescription = result.getDescription();
			inputField.setText("");
		}
		else if (minecraft.currentScreen.equals(this)) {
			this.closeScreen();
		}
	}

	private void resetHelpMessages() {
		helpMessage = new TranslationTextComponent("");
		helpDescription = new ArrayList<>();
	}

	@Override
	public void onClose() {
		super.onClose();
	}
}
