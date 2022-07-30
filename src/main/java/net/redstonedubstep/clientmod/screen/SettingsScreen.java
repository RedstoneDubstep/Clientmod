package net.redstonedubstep.clientmod.screen;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.TooltipAccessor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.redstonedubstep.clientmod.ClientSettings;

public class SettingsScreen extends Screen {

	private OptionsList settingsList;

	public SettingsScreen() {
		super(Component.translatable("screen.clientmod:settingsScreen.name"));
	}

	@Override
	public void init() {
		this.settingsList = new OptionsList(minecraft, width, height, 32, height - 32, 25);
		ClientSettings.updateConfigSettingValues();
		addRenderableWidget(settingsList);
		settingsList.addSmall(ClientSettings.CONFIGS.keySet().toArray(new OptionInstance[]{}));
		settingsList.addSmall(ClientSettings.SETTINGS.toArray(new OptionInstance[]{}));
	}

	@Override
	public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrix);
		font.draw(matrix, title, (width - font.width(title.getString())) / 2, 20, 16777215);
		super.render(matrix, mouseX, mouseY, partialTicks);
		settingsList.render(matrix, mouseX, mouseY, partialTicks);
		renderTooltip(matrix, tooltipAt(settingsList, mouseX, mouseY), mouseX, mouseY);
	}

	public static List<FormattedCharSequence> tooltipAt(OptionsList list, int mouseX, int mouseY) {
		Optional<AbstractWidget> optional = list.getMouseOver(mouseX, mouseY);

		return optional.isPresent() && optional.get() instanceof TooltipAccessor tooltipButton ? tooltipButton.getTooltip() : ImmutableList.of();
	}
}
