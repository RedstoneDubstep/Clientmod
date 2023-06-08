package redstonedubstep.mods.clientmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import redstonedubstep.mods.clientmod.misc.ClientUtility;
import redstonedubstep.mods.clientmod.misc.FieldHolder;
import redstonedubstep.mods.clientmod.misc.WaypointManager;
import redstonedubstep.mods.clientmod.mixin.accessor.EntityAccessor;
import redstonedubstep.mods.clientmod.mixin.accessor.LogoRendererAccessor;
import redstonedubstep.mods.clientmod.mixin.accessor.ScreenAccessor;
import redstonedubstep.mods.clientmod.mixin.accessor.TitleScreenAccessor;
import redstonedubstep.mods.clientmod.platform.ClientSettings;
import redstonedubstep.mods.clientmod.screen.MainScreen;

public class ClientEventHandler {
    public static void onClientTick() {
        if (ClientmodCommon.openTextbox.consumeClick())
            Minecraft.getInstance().setScreen(new MainScreen());

        LocalPlayer player = Minecraft.getInstance().player;

        if (player != null) {
            if (ClientSettings.INSTANCE.renderEntitiesGlowing()) {
                for (Entity entity : player.clientLevel.entitiesForRendering()) {
                    if (entity instanceof LivingEntity living) {
                        living.addEffect(new MobEffectInstance(MobEffects.GLOWING));
                        ((EntityAccessor) living).invokeSetSharedFlag(6, true);
                    }
                }
            }

            if (ClientSettings.INSTANCE.nightVision())
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 210));
            else if (player.hasEffect(MobEffects.NIGHT_VISION) && player.getEffect(MobEffects.NIGHT_VISION).getDuration() <= 0)
                player.removeEffect(MobEffects.NIGHT_VISION);

            if (player.deathTime == 1)
                FieldHolder.lastDeathPosition = player.blockPosition();
        }
    }

    public static boolean onAttackEvent() {
        return ClientSettings.INSTANCE.invincibleVillagers() && !Minecraft.getInstance().gameMode.getPlayerMode().isCreative() && Minecraft.getInstance().hitResult instanceof EntityHitResult hitResult && hitResult.getEntity() instanceof Villager;
    }

    public static void onRightClickBlock(ItemStack useStack, BlockPos pos) {
        if (ClientSettings.INSTANCE.logShulkerPlacement() && useStack.getItem() instanceof BlockItem item && item.getBlock() instanceof ShulkerBoxBlock)
            ClientmodCommon.LOGGER.info("Placed " + item.getBlock().getDescriptionId() + " with name " + useStack.getHoverName() + " at " + ClientUtility.formatBlockPos(pos));
    }

    //play a sound if the "Minceraft" logo is shown
    public static void onInitScreenPost(Screen screen) {
        if (ClientSettings.INSTANCE.notifyWhenMinceraftScreen() && screen instanceof TitleScreen titleScreen) {
            if (((LogoRendererAccessor) ((TitleScreenAccessor) titleScreen).getLogoRenderer()).getShowEasterEgg())
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.WITHER_DEATH, 1, 5));
        }

        if (screen instanceof MerchantScreen) {
            Minecraft minecraft = Minecraft.getInstance();
            int screenLeft = (screen.width - 276) / 2; //imageWidth
            int screenTop = (screen.height - 166) / 2; //imageHeight
            Button closeButton = Button.builder(Component.literal("X"), b -> minecraft.player.closeContainer()).pos(screenLeft + 107 + 50, screenTop + 166 - 94).size(10, 10).build();
            fakeAddRenderableMixin(screen, closeButton);
        }
    }

    private static <T extends GuiEventListener & Renderable & NarratableEntry> void fakeAddRenderableMixin(Screen screen, T widget) {
        ((ScreenAccessor) screen).getRenderables().add(widget);
        ((ScreenAccessor) screen).getChildren().add(widget);
        ((ScreenAccessor) screen).getNarratables().add(widget);
    }

    public static void onRenderGameOverlay(GuiGraphics graphics) {
        Player player = Minecraft.getInstance().player;
        Vec3 lookVec = player.getLookAngle().multiply(1, 0, 1).normalize();

        if (WaypointManager.getInstance().hasWaypoint()) {
            Vec3 relativePos = Vec3.atLowerCornerOf(WaypointManager.getInstance().getWaypoint().subtract(player.blockPosition()));
            float dotProductRaw = (float) lookVec.dot(relativePos.multiply(1, 0, 1).normalize());
            float dotProduct = Math.max(0, dotProductRaw - 0.7F);
            int relativeHeight = Math.round((float) relativePos.y());
            int yOffset = relativeHeight > 3 ? -10 : (relativeHeight < -3 ? 10 : 0);
            int squareSize = relativePos.length() < 150 ? 20 : 10;
            int startX = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - squareSize / 2;
            int startY = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - squareSize / 2 + yOffset;
            int alpha = Math.round(0x11 * dotProduct * 30) << 24;
            int color = alpha + (dotProduct > 0.299 ? 0xFFA500 : 0xFF0000); //default red, orange when the direction is correct

            if (dotProductRaw == 0) {
                color = 0x9900FF00; //green marker when position is reached
            }

            graphics.fill(startX, startY, startX + squareSize, startY + squareSize, color);
        }

        else if (ClientSettings.INSTANCE.speedometer()) {
            double velocity = player.position().subtract(player.xo, player.yo, player.zo).length() * 20;
            int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            graphics.drawString(Minecraft.getInstance().font, String.format("%.2f b/s", velocity), (width + 182) / 2 + 10, height - 16, 0xFFFFFF);
        }
    }
}
