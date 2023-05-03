package redstonedubstep.mods.clientmod.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

public class SettingsScreen extends Screen {
	private OptionsList settingsList;

	public SettingsScreen() {
		super(Component.translatable("screen.clientmod:settingsScreen.name"));
	}

	@Override
	public void init() {
		this.settingsList = new OptionsList(minecraft, width, height, 32, height - 32, 25);
		ClientSettings.INSTANCE.updateOptionInstancesFromConfig();
		addRenderableWidget(settingsList);
		settingsList.setRenderBackground(false);
		settingsList.setRenderTopAndBottom(false);
		settingsList.addSmall(ClientSettings.CONFIGS.keySet().toArray(new OptionInstance[] {}));
		settingsList.addSmall(ClientSettings.SETTINGS.toArray(new OptionInstance[] {}));
	}

	@Override
	public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrix);
		font.draw(matrix, title, (width - font.width(title.getString())) / 2, 20, 16777215);
		super.render(matrix, mouseX, mouseY, partialTicks);
	}
}
