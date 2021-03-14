package net.redstonedubstep.clientmod.screen.button;

import java.util.function.Consumer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class ConfigButton extends Button {
	private final int id;
	private final BooleanValue config;

	public ConfigButton(int id, int xPos, int yPos, int width, int height, BooleanValue config, String translationKey, Consumer<ConfigButton> onClick) {
		this(id, xPos, yPos, width, height, config, new TranslationTextComponent(translationKey), onClick);
	}

	public ConfigButton(int id, int xPos, int yPos, int width, int height, BooleanValue config, ITextComponent displayString, Consumer<ConfigButton> onClick) {
		this(id, xPos, yPos, width, height, config, displayString, onClick, null);
	}

	public ConfigButton(int id, int xPos, int yPos, int width, int height, BooleanValue config, ITextComponent displayString, Consumer<ConfigButton> onClick, ITextComponent tooltip) {
		super(xPos, yPos, width, height, displayString, b -> {
			if(onClick != null)
				onClick.accept((ConfigButton)b);
		}, (b, matrix, x, y) -> {
			if (tooltip != null)
				Minecraft.getInstance().currentScreen.renderTooltip(matrix, tooltip, x, y);
		});

		this.id = id;
		this.config = config;
	}

	//copied from ExtendedButton because we can't extend that due to the tooltip (oh the irony)
	@Override
	public void renderWidget(MatrixStack matrix, int mouseX, int mouseY, float partial)
	{
		if (this.visible)
		{
			Minecraft mc = Minecraft.getInstance();
			this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int k = this.getYImage(this.isHovered());
			GuiUtils.drawContinuousTexturedBox(matrix, WIDGETS_LOCATION, this.x, this.y, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.getBlitOffset());
			this.renderBg(matrix, mc, mouseX, mouseY);

			ITextComponent buttonText = this.getMessage();
			int strWidth = mc.fontRenderer.getStringPropertyWidth(buttonText);
			int ellipsisWidth = mc.fontRenderer.getStringWidth("...");

			if (strWidth > width - 6 && strWidth > ellipsisWidth)
				buttonText = new StringTextComponent(mc.fontRenderer.func_238417_a_(buttonText, width - 6 - ellipsisWidth).getString() + "...");

			drawCenteredString(matrix, mc.fontRenderer, buttonText, this.x + this.width / 2, this.y + (this.height - 8) / 2, getFGColor());

			if (this.isHovered()) {
				this.renderToolTip(matrix, mouseX, mouseY);
			}
		}
	}

	public void updateText() {
		if (getMessage() instanceof TranslationTextComponent) {
			setMessage(new TranslationTextComponent(((TranslationTextComponent)getMessage()).getKey(), new TranslationTextComponent("screen.clientmod:settingsScreen." + (config.get() ? "on" : "off"))));
		}
	}

	public int getId() {
		return id;
	}

	public BooleanValue getConfig() {
		return config;
	}
}
