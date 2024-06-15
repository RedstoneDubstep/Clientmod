package redstonedubstep.mods.clientmod.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

public class SettingsScreen extends OptionsSubScreen {
	public SettingsScreen() {
		super(null, Minecraft.getInstance().options, Component.translatable("screen.clientmod:settingsScreen.name"));
	}

	@Override
	protected void addOptions() {
		ClientSettings.INSTANCE.updateOptionInstancesFromConfig();
		list.addSmall(ClientSettings.CONFIGS.keySet().toArray(new OptionInstance[] {}));
		list.addSmall(ClientSettings.SETTINGS.toArray(new OptionInstance[] {}));
	}
}
