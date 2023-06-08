package redstonedubstep.mods.clientmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

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
		this(name, width, height, width, height, backgroundPath);
		autoResize = true;
	}

	public ImageScreen(String name, int width, int height, int screenWidth, int screenHeight, ResourceLocation backgroundPath) {
		super(Component.translatable(name));

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

		addRenderableWidget(Button.builder(Component.literal("X"), this::closeScreen).pos((width + screenWidth) / 2 - 10, (height - screenHeight) / 2).size(10, 10).build());
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		//RenderSystem.setShaderTexture(0, background); TODO see if needed
		graphics.blit(background, (width - screenWidth) / 2, (height - screenHeight) / 2, xOffset, yOffset, screenWidth, screenHeight, imageWidth, imageHeight);

		super.render(graphics, mouseX, mouseY, partialTicks);
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
		this.onClose();
	}
}
