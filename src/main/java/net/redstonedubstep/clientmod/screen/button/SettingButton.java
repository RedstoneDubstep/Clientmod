package net.redstonedubstep.clientmod.screen.button;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class SettingButton extends ExtendedButton {
	private final Supplier<Boolean> isOn;
	private final int baseHeight;

	public SettingButton(int xPos, int yPos, int width, int height, String translationKey, Consumer<SettingButton> onClick, Supplier<Boolean> isOn) {
		this(xPos, yPos, width, height, Component.translatable(translationKey), onClick, isOn, null);
	}

	public SettingButton(int xPos, int yPos, int width, int height, Component displayString, Consumer<SettingButton> onClick, Supplier<Boolean> isOn, Component tooltip) {
		super(xPos, yPos, width, height, displayString, b -> {
			if (onClick != null)
				onClick.accept((SettingButton)b);
		}, s -> Component.empty());

		setTooltip(Tooltip.create(tooltip));
		this.isOn = isOn;
		this.baseHeight = height;
		updateText();
		validateHeight(width, getMessage());
	}

	public void updateText() {
		if (isOn != null && getMessage() instanceof MutableComponent component && component.getContents() instanceof TranslatableContents content) {
			setMessage(Component.translatable(content.getKey(), Component.translatable("screen.clientmod:settingsScreen." + (isOn.get() ? "on" : "off"))));
			validateHeight(width, getMessage());
		}
	}

	private void validateHeight(int width, Component name) {
		List<FormattedCharSequence> nameLines = Minecraft.getInstance().font.split(name, width - 6);

		this.height = baseHeight + (nameLines.size() - 1) * 12;
	}

	@Override
	public void onPress() {
		super.onPress();
		updateText();
	}
}
