package net.redstonedubstep.clientmod.screen.button;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class SettingButton extends Button {
	private final Supplier<Boolean> isOn;

	public SettingButton(int xPos, int yPos, int width, int height, String translationKey, Consumer<SettingButton> onClick, Supplier<Boolean> isOn) {
		this(xPos, yPos, width, height, new TranslatableComponent(translationKey), onClick, isOn, null);
	}

	public SettingButton(int xPos, int yPos, int width, int height, Component displayString, Consumer<SettingButton> onClick, Supplier<Boolean> isOn, Component tooltip) {
		super(xPos, yPos, width, height, displayString, b -> {
			if(onClick != null)
				onClick.accept((SettingButton)b);
		}, (b, matrix, x, y) -> {
			if (tooltip != null)
				Minecraft.getInstance().screen.renderTooltip(matrix, tooltip, x, y);
		});

		this.isOn = isOn;
		this.updateText();
		this.validateHeight(width, getMessage());
	}

	//copied from ExtendedButton because we can't extend that class (oh the irony) due to the tooltip code missing there
	@Override
	public void renderButton(PoseStack stack, int mouseX, int mouseY, float partial)
	{
		if (this.visible && !this.getMessage().getString().isEmpty())
		{
			Minecraft mc = Minecraft.getInstance();
			Component buttonText = this.getMessage();
			List<FormattedCharSequence> buttonLines = mc.font.split(buttonText, width - 6);

			this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int k = this.getYImage(this.isHovered());
			GuiUtils.drawContinuousTexturedBox(stack, WIDGETS_LOCATION, this.x, this.y, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.getBlitOffset());
			this.renderBg(stack, mc, mouseX, mouseY);

			for (int i = 0; i < buttonLines.size(); i++) {
				FormattedCharSequence line = buttonLines.get(i);

				mc.font.drawShadow(stack, line, this.x + this.width / 2 - mc.font.width(line) / 2, this.y + 6 + i * 12, getFGColor());
			}

			if (this.isHovered()) {
				this.renderToolTip(stack, mouseX, mouseY);
			}
		}
	}

	public void updateText() {
		if (isOn != null && getMessage() instanceof TranslatableComponent) {
			setMessage(new TranslatableComponent(((TranslatableComponent)getMessage()).getKey(), new TranslatableComponent("screen.clientmod:settingsScreen." + (isOn.get() ? "on" : "off"))));
		}
	}

	private void validateHeight(int width, Component name) {
		List<FormattedCharSequence> nameLines = Minecraft.getInstance().font.split(name, width - 6);

		this.height += (nameLines.size() - 1) * 12;
		System.out.println(height);
	}

	@Override
	public void onPress() {
		super.onPress();
		updateText();
	}
}
