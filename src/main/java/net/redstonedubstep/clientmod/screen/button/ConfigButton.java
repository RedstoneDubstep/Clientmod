package net.redstonedubstep.clientmod.screen.button;

import java.util.function.Consumer;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ConfigButton extends SettingButton {
	private final BooleanValue config;

	public ConfigButton(int xPos, int yPos, int width, int height, BooleanValue config, ITextComponent displayString, Consumer<ConfigButton> onClick, ITextComponent tooltip) {
		super(xPos, yPos, width, height, displayString, b -> {
			if(onClick != null)
				onClick.accept((ConfigButton)b);
		}, null, tooltip);

		this.config = config;
	}

	@Override
	public void updateText() {
		if (getMessage() instanceof TranslationTextComponent) {
			setMessage(new TranslationTextComponent(((TranslationTextComponent)getMessage()).getKey(),
					new TranslationTextComponent("screen.clientmod:settingsScreen." + (config.get() ? "on" : "off"))));
		}
	}

	public BooleanValue getConfig() {
		return config;
	}
}
