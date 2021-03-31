package net.redstonedubstep.clientmod.screen.button;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class SettingButton extends Button {
	private final Supplier<Boolean> isOn;

	public SettingButton(int xPos, int yPos, int width, int height, String translationKey, Consumer<SettingButton> onClick, Supplier<Boolean> isOn) {
		this(xPos, yPos, width, height, new TranslationTextComponent(translationKey), onClick, isOn, null);
	}

	public SettingButton(int xPos, int yPos, int width, int height, ITextComponent displayString, Consumer<SettingButton> onClick, Supplier<Boolean> isOn, ITextComponent tooltip) {
		super(xPos, yPos, width, height, displayString, b -> {
			if(onClick != null)
				onClick.accept((SettingButton)b);
		}, (b, matrix, x, y) -> {
			if (tooltip != null)
				Minecraft.getInstance().currentScreen.renderTooltip(matrix, tooltip, x, y);
		});

		this.isOn = isOn;
	}

	//copied from ExtendedButton because we can't extend that class (oh the irony) due to the tooltip code missing there
	@Override
	public void renderWidget(MatrixStack matrix, int mouseX, int mouseY, float partial)
	{
		if (this.visible)
		{
			Minecraft mc = Minecraft.getInstance();
			FontRenderer font = mc.fontRenderer;
			this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int k = this.getYImage(this.isHovered());
			GuiUtils.drawContinuousTexturedBox(matrix, WIDGETS_LOCATION, this.x, this.y, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.getBlitOffset());
			this.renderBg(matrix, mc, mouseX, mouseY);

			ITextComponent buttonText = this.getMessage();
			ITextComponent buttonTextSecondLine = null;
			int strWidth = font.getStringPropertyWidth(buttonText);
			int ellipsisWidth = font.getStringWidth("...");

			if (strWidth > width - 6 && strWidth > ellipsisWidth) { //if the drawn string is longer than the button
				if (height > font.FONT_HEIGHT * 2) {
					String[] elements = buttonText.getString().split(" ");
					StringBuilder buttonTextString = new StringBuilder();
					StringBuilder buttonTextSecondLineString = new StringBuilder();

					boolean isFirstLineFull = false;

					for (String element : elements) {
						if (!isFirstLineFull && font.getStringWidth(buttonTextString + element) < width - 6) {
							buttonTextString.append(element + " ");
						}
						else {
							isFirstLineFull = true;
							buttonTextSecondLineString.append(element + " ");
						}
					}

					buttonText = new StringTextComponent(buttonTextString.toString());
					buttonTextSecondLine = new StringTextComponent(buttonTextSecondLineString.toString());
				}
				else {
					buttonText = new StringTextComponent(font.func_238417_a_(buttonText, width - 6 - ellipsisWidth).getString() + "...");
				}
			}

			if (buttonTextSecondLine != null) {
				drawCenteredString(matrix, font, buttonText, this.x + this.width / 2, this.y + (this.height - 20) / 2, getFGColor());
				drawCenteredString(matrix, font, buttonTextSecondLine, this.x + this.width / 2, this.y + (this.height + 4) / 2, getFGColor());
			}
			else {
				drawCenteredString(matrix, font, buttonText, this.x + this.width / 2, this.y + (this.height - 8) / 2, getFGColor());
			}

			if (this.isHovered()) {
				this.renderToolTip(matrix, mouseX, mouseY);
			}
		}
	}

	public void updateText() {
		if (getMessage() instanceof TranslationTextComponent) {
			setMessage(new TranslationTextComponent(((TranslationTextComponent)getMessage()).getKey(), new TranslationTextComponent("screen.clientmod:settingsScreen." + (isOn.get() ? "on" : "off"))));
		}
	}

	@Override
	public void onPress() {
		super.onPress();
		updateText();
	}
}
