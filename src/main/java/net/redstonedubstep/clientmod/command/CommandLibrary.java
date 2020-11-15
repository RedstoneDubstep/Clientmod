package net.redstonedubstep.clientmod.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.redstonedubstep.clientmod.misc.ClientUtility;
import net.redstonedubstep.clientmod.screen.ImageScreen;

public class CommandLibrary {
	public static ArrayList<Command> commandList = new ArrayList<Command>();
	public static final Command wikiCommand = new Command("wiki", 1, CommandLibrary.Actions::wiki);
	public static final Command imageCommand = new Command("image", 1, CommandLibrary.Actions::image);
	public static final Command msgCommand = new Command("msg", 1, CommandLibrary.Actions::msg);
	public static final Command radarCommand = new Command("radar", 2, CommandLibrary.Actions::radar);
	private static final Minecraft mc = Minecraft.getInstance();

	public static void addCommandsToList() {
		commandList.add(wikiCommand);
		commandList.add(imageCommand);
		commandList.add(msgCommand);
		commandList.add(radarCommand);
	}

	public static CommandResult findAndExecuteCommand(String prefix, String parameter) {
		for (Command command : commandList) {
			if (command.prefix.equals(prefix))
				return command.execute(parameter);
		}
		return CommandResult.PREFIX_NOT_FOUND;
	}

	public static class Actions {
		public static CommandResult wiki(String parameter) {
			parameter = parameter.replace(" ", "_");
			String wiki_link = "https://minecraft.gamepedia.com/"+parameter;
			Util.getOSType().openURI(wiki_link);
			return CommandResult.EXECUTED;
		}

		public static CommandResult image(String parameter) {
			if (parameter.isEmpty())
				return CommandResult.NO_PARAMETER;

			switch (parameter) {
				case "trades": mc.displayGuiScreen(new ImageScreen("trades_screen", 1403, 300, 256, 256, "clientmod:textures/gui/trades_horizontal.png")); break;
				default: return CommandResult.INVALID_PARAMETER;
			}

			return CommandResult.EXECUTED;
		}

		public static CommandResult msg(String parameter) {
			if (parameter.equals("leave"))
				mc.player.sendChatMessage("Redstone has left the server.");
			else
				return CommandResult.INVALID_PARAMETER;

			return CommandResult.EXECUTED;
		}

		public static CommandResult radar(String parameter) { //ik that this is ugly af, but I don't know how to make it prettier yet
			ClientPlayerEntity player = mc.player;
			String[] parameters = parameter.split(" ", 2);
			String distanceIn, entityIn;
			int distance = 0;
			EntityType<?> entityType = null;
			if (parameter.isEmpty())
				distance = 100;
			else {
				distanceIn = parameters[0];
				try {
					distance = Math.min(Integer.parseInt(distanceIn), 100000);
				}catch (NumberFormatException e) {
					return CommandResult.INVALID_PARAMETER;
				}
			}

			if (parameters.length >= 2) {
				entityIn = parameters[1];
				Optional<EntityType<?>> optional = EntityType.byKey(entityIn.replace(" ", "_"));
				if (!optional.isPresent())
					return CommandResult.INVALID_PARAMETER;
				entityType = optional.get();
			}


			if (distance > 0) {
				AxisAlignedBB boundingBox = player.getBoundingBox().grow(distance);

				if (entityType == null) {
					List<Entity> list = mc.world.getEntitiesInAABBexcluding(player, boundingBox, null);
					HashMap<Class<? extends Entity>, Integer> map = ClientUtility.countEntitiesInList(list);

					player.sendMessage(new StringTextComponent("These are all the mobs in " + distance + " blocks range:"), Util.DUMMY_UUID);
					map.forEach((key, value) -> {
						player.sendMessage(new StringTextComponent("- " + value + " " + key.getSimpleName()), Util.DUMMY_UUID);
					});
				} else {
					List<? extends Entity> list = mc.world.getEntitiesWithinAABB(entityType, boundingBox, (s) -> true);

					player.sendMessage(new StringTextComponent("These are all the mobs of type "+new TranslationTextComponent(entityType.toString()).getString()+" in " + distance + " blocks range:"), Util.DUMMY_UUID);
					list.forEach((entry) -> player.sendMessage(new StringTextComponent("- " + entry.getName().getString() + " ("+ClientUtility.formatBlockPos(entry.getPosition())+")"), Util.DUMMY_UUID));
				}
			}
			else
				return CommandResult.INVALID_PARAMETER;

			return CommandResult.EXECUTED;
		}
	}
}
