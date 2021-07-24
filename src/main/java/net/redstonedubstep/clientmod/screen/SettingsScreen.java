package net.redstonedubstep.clientmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.AbstractOption;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.util.text.TranslationTextComponent;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.ClientSettings.BetterBooleanOption;
import net.redstonedubstep.clientmod.screen.button.SettingButton;

public class SettingsScreen extends Screen {

	private OptionsRowList settingsList;
	private final AbstractOption[] configs = {new BetterBooleanOption(ClientSettings.CONFIG.notifyWhenMinceraftScreen), new BetterBooleanOption(ClientSettings.CONFIG.shouldReloadSounds), new BetterBooleanOption(ClientSettings.CONFIG.drawReloadingBackground), new BetterBooleanOption(ClientSettings.CONFIG.enhancedReloadingInfo)};
	private final AbstractOption[] settings = {ClientSettings.SEND_MESSAGES_WITH_TEAMMSG};

	public SettingsScreen() {
		super(new TranslationTextComponent("screen.clientmod:settingsScreen.name"));
	}

	@Override
	public void init() {
		this.settingsList = new OptionsRowList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
		children.add(settingsList);
		settingsList.addOptions(configs);
		settingsList.addOptions(settings);
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrix);
		font.drawText(matrix, title, (width - font.getStringWidth(title.getString())) / 2, 20, 16777215);
		super.render(matrix, mouseX, mouseY, partialTicks);
		settingsList.renderList(matrix, this.width / 2 - settingsList.getRowWidth() / 2 + 2, 32, mouseX, mouseY, partialTicks);
	}

	@Override
	protected <T extends Widget> T addButton(T button) {
		if (button instanceof SettingButton) {
			((SettingButton)button).updateText();
		}

		return super.addButton(button);
	}
}
