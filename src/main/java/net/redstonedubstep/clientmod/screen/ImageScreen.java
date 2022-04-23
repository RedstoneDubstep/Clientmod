package net.redstonedubstep.clientmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class ImageScreen extends Screen {
	private final ResourceLocation background;
	boolean horizontalScrolling;
	boolean verticalScrolling;
	int imageWidth;
	int screenWidth;
	int imageHeight;
	int screenHeight;
	int xOffset = 0;
	int yOffset = 0;
	boolean autoResize = false;

	public ImageScreen(String name, ResourceLocation backgroundPath) {
		this(name, 300, 300, backgroundPath);
	}

	public ImageScreen(String name, int width, int height, ResourceLocation backgroundPath) {
		this(name, width, height, width ,height, backgroundPath);
		autoResize = true;
	}

	public ImageScreen(String name, int width, int height, int screenWidth, int screenHeight, ResourceLocation backgroundPath) {
		super(new TranslationTextComponent(name));

		background = backgroundPath;
		this.imageWidth = width;
		this.imageHeight = height;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	@Override
	public void init() {
		if (autoResize) {
			screenWidth = Math.min(minecraft.getWindow().getGuiScaledWidth() - 20, imageWidth);
			screenHeight = Math.min(minecraft.getWindow().getGuiScaledHeight() - 20, imageHeight);
			horizontalScrolling = screenWidth < imageWidth;
			verticalScrolling = screenHeight < imageHeight;
		}

		addButton(new ExtendedButton((width + screenWidth) / 2 - 10, (height - screenHeight) / 2, 10, 10, new StringTextComponent("X"), this::closeScreen));
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bind(background);
		RenderSystem.enableAlphaTest();
		blit(matrix, (width - screenWidth) / 2, (height - screenHeight) / 2, xOffset,yOffset, screenWidth, imageHeight, imageWidth, imageHeight);
		RenderSystem.disableAlphaTest();

		super.render(matrix, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		super.mouseScrolled(mouseX, mouseY, delta);

		if ((hasShiftDown() || !verticalScrolling) && horizontalScrolling)
			xOffset += (delta == -1 ? 24 : -24);
		else if (verticalScrolling)
			yOffset += (delta == -1 ? 24 : -24);
		return true;
	}

	public void closeScreen(Button button) {
		onClose();
	}
}
