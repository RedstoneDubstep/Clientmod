package net.redstonedubstep.clientmod;

import com.google.common.base.Predicates;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.redstonedubstep.clientmod.screen.ImageScreen;

public class ClientCommand {
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		System.out.println("register in ClientCommand");
		dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("cc")
				.requires(Predicates.alwaysTrue())
				.then(connect())
				.then(help())
				.then(bug())
				.then(Commands.literal("images").then(trades()))
				.then(Commands.literal("server").then(creeper())).then(Commands.literal("misc").then(wiki())));
	}

	private static ArgumentBuilder<CommandSource, ?> connect()
	{
		return Commands.literal("connect").executes(ctx -> {
			ctx.getSource().asPlayer().sendMessage(new StringTextComponent("[")
					.append(new StringTextComponent("IRC").mergeStyle(TextFormatting.GREEN))
					.append(new StringTextComponent("] "))
					.append(new StringTextComponent(" ")), Util.DUMMY_UUID); //appendSibling
			return 0;
		});
	}

	private static ArgumentBuilder<CommandSource, ?> help()
	{
		return Commands.literal("help").executes(ctx -> {
			ctx.getSource().asPlayer().sendMessage(new TranslationTextComponent("messages.securitycraft:sc_help",
					new TranslationTextComponent(Blocks.CRAFTING_TABLE.getTranslationKey()),
					new TranslationTextComponent(Items.BOOK.getTranslationKey()),
					new TranslationTextComponent(Items.IRON_BARS.getTranslationKey())), Util.DUMMY_UUID);
			return 0;
		});
	}

	private static ArgumentBuilder<CommandSource, ?> bug()
	{
		return Commands.literal("bug").executes(ctx -> {
			return 0;
		});
	}

	private static ArgumentBuilder<CommandSource, ?> trades() {
		return Commands.literal("trades").executes(ctx -> {
			if(!ctx.getSource().getWorld().isRemote)
				Minecraft.getInstance().displayGuiScreen(new ImageScreen("trades_screen", 1403, 300, 256, 256, "clientmod:textures/gui/trades_horizontal.png"));
			return 0;
		});
	}

	private static ArgumentBuilder<CommandSource, ?> creeper() {
		return Commands.literal("creeper").executes(ctx -> {
			if (ctx.getSource().getWorld().isRemote()) {
				BlockPos pos = new BlockPos(ctx.getSource().getPos());
				World world = ctx.getSource().getWorld();
				CreeperEntity creeper = EntityType.CREEPER.create(world);

				creeper.setPosition(pos.getX(), pos.getY(), pos.getZ());
				world.addEntity(creeper);
			}
			return 0;
		});
	}

	private static ArgumentBuilder<CommandSource, ?> wiki() {
		return Commands.literal("wiki").then(Commands.argument("topic", MessageArgument.message()).executes(ctx -> {
			PlayerEntity player = ctx.getSource().asPlayer();
			ITextComponent argument = MessageArgument.getMessage(ctx, "topic");
			String link_path = format(argument.getString());
			String link = "https://minecraft.gamepedia.com/"+link_path;
			Util.getOSType().openURI(link);
			return 0;
		}));
	}

	private static String format(String string) {
		return string.replace(" ", "_").toLowerCase();
	}
}
