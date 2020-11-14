package net.redstonedubstep.clientmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;
import net.redstonedubstep.clientmod.command.CommandLibrary;
import net.redstonedubstep.clientmod.command.CommandResult;
import org.lwjgl.glfw.GLFW;

public class MainScreen extends Screen {

	TextFieldWidget inputField;
	String helpMessage = "";
	Minecraft mc = Minecraft.getInstance();

	public MainScreen() {
		super(new StringTextComponent("main_screen"));
	}

	@Override
	public void init(){
		super.init();

		minecraft.keyboardListener.enableRepeatEvents(true);
		inputField = new TextFieldWidget(font, width / 2 - 70, height / 2 - 10, 140, 20, StringTextComponent.EMPTY);
		inputField.setTextColor(-1);
		inputField.setDisabledTextColour(-1);
		inputField.setEnableBackgroundDrawing(true);
		inputField.setFocused2(true);

	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks){
		renderBackground(matrix);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		inputField.render(matrix, mouseX, mouseY, partialTicks);
		font.func_243248_b(matrix, new StringTextComponent(helpMessage), (width - font.getStringWidth(helpMessage)) / 2, (height+30)/2, 16711680);
		super.render(matrix, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int p_keyPressed_3_) {
		if(inputField.isFocused()) {
			if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
				if (inputField.getText().isEmpty())
					return false;
			}

			if(keyCode == mc.gameSettings.keyBindInventory.getKey().getKeyCode())
				return false;
			else if(keyCode == GLFW.GLFW_KEY_ESCAPE)
				return super.keyPressed(keyCode, scanCode, p_keyPressed_3_);
			else if(keyCode == GLFW.GLFW_KEY_ENTER)
				return submitText(inputField.getText());
			else
				return inputField.keyPressed(keyCode, scanCode, p_keyPressed_3_);
		}
		else return super.keyPressed(keyCode, scanCode, p_keyPressed_3_);
	}

	@Override
	public boolean charTyped(char typedChar, int keyCode) {
		if(inputField.isFocused()) {
			inputField.charTyped(typedChar, keyCode);
			return true;
		}
		else
			return super.charTyped(typedChar, keyCode);
	}

	private boolean submitText(String input) {
		if (input.isEmpty())
			setHelpMessage(CommandResult.EMPTY);

		String prefix = input.split(" ")[0];
		String parameter = input.contains(" ") ? input.split(" ", 2)[1] : "";

		CommandResult result = CommandLibrary.findAndExecuteCommand(prefix, parameter);
		setHelpMessage(result);

		return true;
	}

	public void setHelpMessage(CommandResult result) {
		if (result != CommandResult.EXECUTED) {
			helpMessage = result.getMessage();
			inputField.setText("");
		}
		else
			this.closeScreen();
	}

	@Override
	public void onClose() {
		super.onClose();
	}
}
