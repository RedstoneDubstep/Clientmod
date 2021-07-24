package net.redstonedubstep.clientmod.screen.button;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
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
		this.updateText();
		this.validateHeight(width, getMessage());
	}

	//copied from ExtendedButton because we can't extend that class (oh the irony) due to the tooltip code missing there
	@Override
	public void renderWidget(MatrixStack stack, int mouseX, int mouseY, float partial)
	{
		if (this.visible && !this.getMessage().getString().isEmpty())
		{
			Minecraft mc = Minecraft.getInstance();
			ITextComponent buttonText = this.getMessage();
			List<IReorderingProcessor> buttonLines = mc.fontRenderer.trimStringToWidth(buttonText, width - 6);

			this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int k = this.getYImage(this.isHovered());
			GuiUtils.drawContinuousTexturedBox(stack, WIDGETS_LOCATION, this.x, this.y, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.getBlitOffset());
			this.renderBg(stack, mc, mouseX, mouseY);

			for (int i = 0; i < buttonLines.size(); i++) {
				IReorderingProcessor line = buttonLines.get(i);

				mc.fontRenderer.drawTextWithShadow(stack, line, this.x + this.width / 2 - mc.fontRenderer.func_243245_a(line) / 2, this.y + 6 + i * 12, getFGColor());
			}

			if (this.isHovered()) {
				this.renderToolTip(stack, mouseX, mouseY);
			}
		}
	}

	public void updateText() {
		if (isOn != null && getMessage() instanceof TranslationTextComponent) {
			setMessage(new TranslationTextComponent(((TranslationTextComponent)getMessage()).getKey(), new TranslationTextComponent("screen.clientmod:settingsScreen." + (isOn.get() ? "on" : "off"))));
		}
	}

	private void validateHeight(int width, ITextComponent name) {
		List<IReorderingProcessor> nameLines = Minecraft.getInstance().fontRenderer.trimStringToWidth(name, width - 6);

		this.height += (nameLines.size() - 1) * 12;
		System.out.println(height);
	}

	@Override
	public void onPress() {
		super.onPress();
		updateText();
	}
}
