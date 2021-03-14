package net.redstonedubstep.clientmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.redstonedubstep.clientmod.ClientConfig;
import net.redstonedubstep.clientmod.screen.button.IdButton;

public class SettingsScreen extends Screen {

	public SettingsScreen() {
		super(new TranslationTextComponent("screen.clientmod:settingsScreen.name"));
	}

	@Override
	public void init() {
		addButton(new IdButton(0, 100, 40, 120, 22, new TranslationTextComponent("screen.clientmod:settingsScreen.reloadSounds", onOrOff(ClientConfig.CLIENT.shouldReloadSounds)), this::actionPerformed, new TranslationTextComponent("config.clientmod:shouldReloadSounds.description")));
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrix);
		font.drawText(matrix, title, (width - font.getStringWidth(title.getString())) / 2, 20, 16777215);
		super.render(matrix, mouseX, mouseY, partialTicks);
	}

	private void actionPerformed(IdButton button) {
		int id = button.getId();

		if (id == 0) { //reload sounds button
			ClientConfig.CLIENT.shouldReloadSounds.set(!ClientConfig.CLIENT.shouldReloadSounds.get());
			buttons.get(0).setMessage(new TranslationTextComponent("screen.clientmod:settingsScreen.reloadSounds", onOrOff(ClientConfig.CLIENT.shouldReloadSounds)));
		}
	}

	public TranslationTextComponent onOrOff(BooleanValue value) {
		return new TranslationTextComponent("screen.clientmod:settingsScreen." + (value.get() ? "on" : "off"));
	}

}
