package redstonedubstep.mods.clientmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionResult;
import redstonedubstep.mods.clientmod.platform.FabricClientSettings;

public class ClientmodFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientmodCommon.init();
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> ClientmodCommon.onClientStarted());
        ClientTickEvents.END_CLIENT_TICK.register(client -> ClientEventHandler.onClientTick());
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> world instanceof ClientLevel && ClientEventHandler.onAttackEvent() ? InteractionResult.FAIL : InteractionResult.PASS);
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            ClientEventHandler.onRightClickBlock(player.getItemInHand(hand), hitResult.getBlockPos());
            return InteractionResult.PASS;
        });
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> ClientEventHandler.onInitScreenPost(screen));
        KeyBindingHelper.registerKeyBinding(ClientmodCommon.openTextbox);
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> ClientEventHandler.onRenderGameOverlay(graphics));
        ((FabricClientSettings) FabricClientSettings.INSTANCE).loadConfig();
    }
}
