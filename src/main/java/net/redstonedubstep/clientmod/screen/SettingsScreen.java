package net.redstonedubstep.clientmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.util.text.TranslationTextComponent;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.ClientSettings.BetterBooleanOption;
import net.redstonedubstep.clientmod.screen.button.SettingButton;

public class SettingsScreen extends Screen {

	private OptionsRowList settingsList;

	public SettingsScreen() {
		super(new TranslationTextComponent("screen.clientmod:settingsScreen.name"));
	}

	@Override
	public void init() {
		this.settingsList = new OptionsRowList(minecraft, width, height, 32, height - 32, 25);
		children.add(settingsList);
		settingsList.addSmall(ClientSettings.CONFIGS.toArray(new BetterBooleanOption[]{}));
		settingsList.addSmall(ClientSettings.SETTINGS.toArray(new BetterBooleanOption[]{}));
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrix);
		font.draw(matrix, title, (width - font.width(title.getString())) / 2, 20, 16777215);
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
