package redstonedubstep.mods.clientmod.mixin.rendering;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import redstonedubstep.mods.clientmod.platform.ClientSettings;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    private void clientmod$onRenderSpyglassOverlay(GuiGraphics graphics, float scopeScale, CallbackInfo callbackInfo) {
        if (!ClientSettings.INSTANCE.renderSpyglassOverlay())
            callbackInfo.cancel();
    }
}
