package net.redstonedubstep.clientmod.screen;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.screen.button.ConfigButton;
import net.redstonedubstep.clientmod.screen.button.SettingButton;

public class SettingsScreen extends Screen {

	public SettingsScreen() {
		super(new TranslationTextComponent("screen.clientmod:settingsScreen.name"));
	}

	@Override
	public void init() {
		addPositionedConfigButton(1, 1, ClientSettings.CONFIG.shouldReloadSounds, "config.clientmod:shouldReloadSounds", this::configButtonClicked);
		addPositionedConfigButton(1, 2, ClientSettings.CONFIG.drawReloadingBackground, "config.clientmod:disableReloadingBackground", this::configButtonClicked);

		addPositionedConfigButton(2, 1, ClientSettings.CONFIG.notifyWhenMinceraftScreen, "config.clientmod:notifyWhenMinceraftScreen", this::configButtonClicked);

		addPositionedSettingButton(3, 1, "screen.clientmod:settingsScreen.button.sendMessagesWithTeammsg", b -> ClientSettings.sendMessagesWithTeammsg = !ClientSettings.sendMessagesWithTeammsg, () -> ClientSettings.sendMessagesWithTeammsg);
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrix);
		font.drawText(matrix, title, (width - font.getStringWidth(title.getString())) / 2, 20, 16777215);
		super.render(matrix, mouseX, mouseY, partialTicks);
	}

	private void configButtonClicked(ConfigButton button) {
		BooleanValue config = button.getConfig();

		config.set(!config.get());
	}

	public TranslationTextComponent onOrOff(BooleanValue value) {
		return new TranslationTextComponent("screen.clientmod:settingsScreen." + (value.get() ? "on" : "off"));
	}

	private void addPositionedSettingButton(int row, int column, String translationKey, Consumer<SettingButton> onClick, Supplier<Boolean> isOn) {
		TranslationTextComponent buttonText = new TranslationTextComponent(translationKey + ".name");
		TranslationTextComponent tooltip = new TranslationTextComponent(translationKey + ".description");
		boolean twoRows = font.getStringPropertyWidth(buttonText) >= 150;

		addButton(new SettingButton((width + (column - 2) * 350 - 175) / 2, 20 + row * 30, 150, twoRows ? 32 : 22, buttonText, onClick, isOn, tooltip));
	}

	private void addPositionedConfigButton(int row, int column, BooleanValue config, String translationKey, Consumer<ConfigButton> onClick) {
		TranslationTextComponent buttonText = new TranslationTextComponent(translationKey + ".name");
		TranslationTextComponent tooltip = new TranslationTextComponent(translationKey + ".description");

		buttonText.mergeStyle(TextFormatting.RED);
		addButton(new ConfigButton((width + (column - 2) * 350 - 175) / 2, 20 + row * 30, 150, 22, config, buttonText, onClick, tooltip));
	}

	@Override
	protected <T extends Widget> T addButton(T button) {
		if (button instanceof SettingButton) {
			((SettingButton)button).updateText();
		}

		return super.addButton(button);
	}
}
