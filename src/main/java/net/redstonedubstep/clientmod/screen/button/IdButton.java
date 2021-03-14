package net.redstonedubstep.clientmod.screen.button;

import java.util.function.Consumer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class IdButton extends Button {
	private final int id;

	public IdButton(int id, int xPos, int yPos, int width, int height, String translationKey, Consumer<IdButton> onClick) {
		this(id, xPos, yPos, width, height, new TranslationTextComponent(translationKey), onClick);
	}

	public IdButton(int id, int xPos, int yPos, int width, int height, ITextComponent displayString, Consumer<IdButton> onClick) {
		super(xPos, yPos, width, height, displayString, b -> {
			if(onClick != null)
				onClick.accept((IdButton)b);
		});

		this.id = id;
	}

	public IdButton(int id, int xPos, int yPos, int width, int height, ITextComponent displayString, Consumer<IdButton> onClick, ITextComponent tooltip) {
		super(xPos, yPos, width, height, displayString, b -> {
			if(onClick != null)
				onClick.accept((IdButton)b);
		}, (b, matrix, x, y) -> {
			Minecraft.getInstance().currentScreen.renderTooltip(matrix, tooltip, x, y);
		});

		this.id = id;
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
				//TODO, srg names make it hard to figure out how to append to an ITextProperties from this trim operation, wraping this in StringTextComponent is kinda dirty.
				buttonText = new StringTextComponent(mc.fontRenderer.func_238417_a_(buttonText, width - 6 - ellipsisWidth).getString() + "...");

			drawCenteredString(matrix, mc.fontRenderer, buttonText, this.x + this.width / 2, this.y + (this.height - 8) / 2, getFGColor());

			if (this.isHovered()) {
				this.renderToolTip(matrix, mouseX, mouseY);
			}
		}
	}

	public int getId() {
		return id;
	}
}
