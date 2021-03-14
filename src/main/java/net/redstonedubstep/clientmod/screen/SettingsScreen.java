package net.redstonedubstep.clientmod.screen;

import java.util.function.Consumer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.redstonedubstep.clientmod.ClientConfig;
import net.redstonedubstep.clientmod.screen.button.ConfigButton;

public class SettingsScreen extends Screen {

	public SettingsScreen() {
		super(new TranslationTextComponent("screen.clientmod:settingsScreen.name"));
	}

	@Override
	public void init() {
		addPositionedSettingsButton(0, 1, 1, ClientConfig.CLIENT.shouldReloadSounds, "config.clientmod:shouldReloadSounds.name", this::actionPerformed, new TranslationTextComponent("config.clientmod:shouldReloadSounds.description"));
		addPositionedSettingsButton(1, 1, 2, ClientConfig.CLIENT.drawReloadingBackground, "config.clientmod:disableReloadingBackground.name", this::actionPerformed, new TranslationTextComponent("config.clientmod:disableReloadingBackground.description"));
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrix);
		font.drawText(matrix, title, (width - font.getStringWidth(title.getString())) / 2, 20, 16777215);
		super.render(matrix, mouseX, mouseY, partialTicks);
	}

	private void actionPerformed(ConfigButton button) {
		BooleanValue config = button.getConfig();

		config.set(!config.get());
		button.updateText();
	}

	public TranslationTextComponent onOrOff(BooleanValue value) {
		return new TranslationTextComponent("screen.clientmod:settingsScreen." + (value.get() ? "on" : "off"));
	}

	private void addPositionedSettingsButton(int id, int row, int column, BooleanValue config, String translationKey, Consumer<ConfigButton> onClick, ITextComponent tooltip) {
		TranslationTextComponent buttonText = new TranslationTextComponent(translationKey, onOrOff(config));
		int buttonWidth = font.getStringWidth(buttonText.getString()) + 10;

		addButton(new ConfigButton(id, (width + (column - 2) * 300) / 2, 20 + row * 30, buttonWidth, 22, config, buttonText, onClick, tooltip));
	}

}
