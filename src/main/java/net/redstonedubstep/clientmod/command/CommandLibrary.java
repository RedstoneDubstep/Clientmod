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
import net.redstonedubstep.clientmod.command.parameter.AbstractParameter;
import net.redstonedubstep.clientmod.command.parameter.IntParameter;
import net.redstonedubstep.clientmod.command.parameter.StringParameter;
import net.redstonedubstep.clientmod.misc.ClientUtility;
import net.redstonedubstep.clientmod.screen.ImageScreen;

public class CommandLibrary {
	public static ArrayList<Command> commandList = new ArrayList<>();
	public static final Command WIKI_COMMAND = new Command("wiki", CommandLibrary.Actions::wiki, new StringParameter());
	public static final Command IMAGE_COMMAND = new Command("image", CommandLibrary.Actions::image, new StringParameter());
	public static final Command MSG_COMMAND = new Command("msg", CommandLibrary.Actions::msg, new StringParameter());
	public static final Command RADAR_COMMAND = new Command("radar", CommandLibrary.Actions::radar, new IntParameter(false, 100, 10000), new StringParameter(false));
	private static final Minecraft mc = Minecraft.getInstance();

	public static void addCommandsToList() {
		commandList.add(WIKI_COMMAND);
		commandList.add(IMAGE_COMMAND);
		commandList.add(MSG_COMMAND);
		commandList.add(RADAR_COMMAND);
	}

	public static CommandResult findAndExecuteCommand(String prefix, String parameter) {
		for (Command command : commandList) {
			if (command.prefix.equals(prefix))
				return command.execute(parameter);
		}
		return CommandResult.PREFIX_NOT_FOUND;
	}

	public static class Actions {
		private static CommandResult wiki(AbstractParameter<?>[] params) {
			String text = ((StringParameter)params[0]).getValue().replace(" ", "_");
			String wiki_link = "https://minecraft.gamepedia.com/"+text;

			Util.getOSType().openURI(wiki_link);
			return CommandResult.EXECUTED;
		}

		private static CommandResult image(AbstractParameter<?>[] params) {
			String text = ((StringParameter)params[0]).getValue();

			switch (text) {
				case "trades": mc.displayGuiScreen(new ImageScreen("trades_screen", 1403, 300, 256, 256, "clientmod:textures/gui/trades_horizontal.png")); break;
				default: return CommandResult.INVALID_PARAMETER;
			}

			return CommandResult.EXECUTED;
		}

		private static CommandResult msg(AbstractParameter<?>[] params) {
			String text = ((StringParameter)params[0]).getValue();

			if (text.equals("leave"))
				mc.player.sendChatMessage("Redstone has left the server.");
			else
				return CommandResult.INVALID_PARAMETER;

			return CommandResult.EXECUTED;
		}

		private static CommandResult radar(AbstractParameter<?> [] params) {
			ClientPlayerEntity player = mc.player;
			int range = ((IntParameter)params[0]).getValue();
			String entityName = ((StringParameter)params[1]).getValue().replace(" ", "_");
			Optional<EntityType<?>> optional = EntityType.byKey(entityName);
			EntityType<?> entity;
			AxisAlignedBB boundingBox = player.getBoundingBox().grow(range);

			if (!entityName.isEmpty() && !optional.isPresent()) {
				return CommandResult.INVALID_PARAMETER;
			} else {
				entity = optional.orElse(null);
			}

			if (entity == null) {
				List<Entity> list = mc.world.getEntitiesInAABBexcluding(player, boundingBox, null);
				HashMap<Class<? extends Entity>, Integer> map = ClientUtility.countEntitiesInList(list);

				player.sendMessage(new StringTextComponent("These are all the mobs in " + range + " blocks range:"), Util.DUMMY_UUID);
				map.forEach((key, value) -> {
					player.sendMessage(new StringTextComponent("- " + value + " " + key.getSimpleName()), Util.DUMMY_UUID);
				});
			} else {
				List<? extends Entity> list = mc.world.getEntitiesWithinAABB(entity, boundingBox, (s) -> true);

				player.sendMessage(new StringTextComponent("These are all the mobs of type "+new TranslationTextComponent(entity.toString()).getString()+" in " + range + " blocks range:"), Util.DUMMY_UUID);
				list.forEach((entry) -> player.sendMessage(new StringTextComponent("- " + entry.getName().getString() + " ("+ClientUtility.formatBlockPos(entry.getPosition())+")"), Util.DUMMY_UUID));
			}

			return CommandResult.EXECUTED;
		}
	}
}
