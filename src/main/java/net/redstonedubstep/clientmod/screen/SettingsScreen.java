package net.redstonedubstep.clientmod.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.redstonedubstep.clientmod.ClientSettings;
import net.redstonedubstep.clientmod.ClientSettings.BetterBooleanOption;
import net.redstonedubstep.clientmod.screen.button.SettingButton;

public class SettingsScreen extends Screen {

	private OptionsList settingsList;

	public SettingsScreen() {
		super(new TranslatableComponent("screen.clientmod:settingsScreen.name"));
	}

	@Override
	public void init() {
		this.settingsList = new OptionsList(minecraft, width, height, 32, height - 32, 25);
		addRenderableWidget(settingsList);
		settingsList.addSmall(ClientSettings.CONFIGS.toArray(new BetterBooleanOption[]{}));
		settingsList.addSmall(ClientSettings.SETTINGS.toArray(new BetterBooleanOption[]{}));
	}

	@Override
	public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrix);
		font.draw(matrix, title, (width - font.width(title.getString())) / 2, 20, 16777215);
		super.render(matrix, mouseX, mouseY, partialTicks);
		settingsList.renderList(matrix, this.width / 2 - settingsList.getRowWidth() / 2 + 2, 32, mouseX, mouseY, partialTicks);
	}

	@Override
	protected <T extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidget(T button) {
		if (button instanceof SettingButton) {
			((SettingButton)button).updateText();
		}

		return super.addRenderableWidget(button);
	}
}
