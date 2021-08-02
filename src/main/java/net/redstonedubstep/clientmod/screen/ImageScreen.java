package net.redstonedubstep.clientmod.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.minecraftforge.fmlclient.gui.widget.ExtendedButton;

public class ImageScreen extends Screen {
	private final ResourceLocation background;
	boolean horizontalScrolling;
	boolean verticalScrolling;
	int imageWidth;
	int screenWidth;
	int imageHeight;
	int screenHeight;
	int offset = 0;

	public ImageScreen(String name, int width, int height, String backgroundPath) {
		this(name, width, height, width ,height, backgroundPath);
	}

	public ImageScreen(String name, int width, int height, int screenWidth, int screenHeight, String backgroundPath) {
		super(new TranslatableComponent(name));

		background = new ResourceLocation(backgroundPath);
		this.imageWidth = width;
		this.imageHeight = height;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.horizontalScrolling = screenWidth < width;
		this.verticalScrolling = screenHeight < height;
	}

	@Override
	public void init() {
		addButton(new ExtendedButton((width + screenWidth) / 2 - 10, (height - imageHeight) / 2, 10, 10, new TextComponent("X"), this::closeScreen));
	}

	@Override
	public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, background);
		blit(matrix, (width - screenWidth) / 2, (height - imageHeight) / 2, horizontalScrolling ? offset : 0, verticalScrolling ? offset : 0, screenWidth, imageHeight, imageWidth, imageHeight);
		RenderSystem.disableAlphaTest();

		super.render(matrix, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		super.mouseScrolled(mouseX, mouseY, delta);

		if (horizontalScrolling || verticalScrolling) {
			switch ((int)Math.signum(delta)) {
				case -1: offset += 24; break;
				case 1: offset -= 24; break;
			}
		}
		return true;
	}

	public void closeScreen(Button button) {
		this.onClose();
	}
}
