package net.redstonedubstep.clientmod.screen.button;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.GuiUtils;

public class SettingButton extends Button {
	private final Supplier<Boolean> isOn;
	private final int baseHeight;

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
		this.baseHeight = height;
		updateText();
		validateHeight(width, getMessage());
	}

	//copied from ExtendedButton because we can't extend that class (oh the irony) due to the tooltip code missing there
	@Override
	public void renderButton(PoseStack stack, int mouseX, int mouseY, float partial)
	{
		if (visible && !getMessage().getString().isEmpty()) {
			Minecraft mc = Minecraft.getInstance();
			Component buttonText = getMessage();
			List<FormattedCharSequence> buttonLines = mc.font.split(buttonText, width - 6);

			isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			int k = getYImage(isHoveredOrFocused());
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			GuiUtils.drawContinuousTexturedBox(stack, WIDGETS_LOCATION, x, y, 0, 46 + k * 20, width, height, 200, 20, 2, 3, 2, 2, getBlitOffset());
			renderBg(stack, mc, mouseX, mouseY);

			for (int i = 0; i < buttonLines.size(); i++) {
				FormattedCharSequence line = buttonLines.get(i);

				mc.font.drawShadow(stack, line, x + width / 2 - mc.font.width(line) / 2, y + 6 + i * 12, getFGColor());
			}

			if (isHoveredOrFocused()) {
				renderToolTip(stack, mouseX, mouseY);
			}
		}
	}

	public void updateText() {
		if (isOn != null && getMessage() instanceof TranslatableComponent) {
			setMessage(new TranslatableComponent(((TranslatableComponent)getMessage()).getKey(), new TranslatableComponent("screen.clientmod:settingsScreen." + (isOn.get() ? "on" : "off"))));
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
