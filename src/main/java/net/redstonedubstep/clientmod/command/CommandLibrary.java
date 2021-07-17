package net.redstonedubstep.clientmod.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.redstonedubstep.clientmod.command.parameter.AbstractParameter;
import net.redstonedubstep.clientmod.command.parameter.EntityTypeParameter;
import net.redstonedubstep.clientmod.command.parameter.IntParameter;
import net.redstonedubstep.clientmod.command.parameter.StringParameter;
import net.redstonedubstep.clientmod.misc.ClientUtility;
import net.redstonedubstep.clientmod.misc.FieldHolder;
import net.redstonedubstep.clientmod.misc.WaypointManager;
import net.redstonedubstep.clientmod.screen.ImageScreen;
import net.redstonedubstep.clientmod.screen.SettingsScreen;

public class CommandLibrary {
	public static ArrayList<Command> commandList = new ArrayList<>();
	private static final Command FOLDER_COMMAND = new Command("folder", CommandLibrary.Actions::folder, new StringParameter(Lists.newArrayList("resources", "mods", "mc")));
	private static final Command IMAGE_COMMAND = new Command("image", CommandLibrary.Actions::image, new StringParameter(Lists.newArrayList("trades", "brewing")));
	private static final Command LOG_COMMAND = new Command("log", CommandLibrary.Actions::log, new StringParameter(Lists.newArrayList("lastDeath")));
	private static final Command MSG_COMMAND = new Command("msg", CommandLibrary.Actions::msg, new StringParameter());
	private static final Command NAMEMC_COMMAND = new Command("namemc", CommandLibrary.Actions::namemc, new StringParameter());
	private static final Command RADAR_COMMAND = new Command("radar", CommandLibrary.Actions::radar, new IntParameter(false, 100, 10000, 0), new EntityTypeParameter(false));
	private static final Command SETTINGS_COMMAND = new Command("settings", CommandLibrary.Actions::settings);
	private static final Command WAYPOINT_COMMAND = new Command("waypoint", CommandLibrary.Actions::waypoint, new StringParameter(Lists.newArrayList("set", "get", "remove"), false, ""), new IntParameter(false, null), new IntParameter(false, null), new IntParameter(false, null));
	private static final Command WIKI_COMMAND = new Command("wiki", CommandLibrary.Actions::wiki, new StringParameter());
	public static String lastInputText;
	private static final Minecraft mc = Minecraft.getInstance();

	public static void addCommandsToList() {
		commandList.add(FOLDER_COMMAND);
		commandList.add(IMAGE_COMMAND);
		commandList.add(LOG_COMMAND);
		//commandList.add(MSG_COMMAND);
		commandList.add(NAMEMC_COMMAND);
		commandList.add(RADAR_COMMAND);
		commandList.add(SETTINGS_COMMAND);
		commandList.add(WAYPOINT_COMMAND);
		commandList.add(WIKI_COMMAND);
	}

	public static CommandException findAndExecuteCommand(String prefix, String parameter) {
		for (Command command : commandList) {
			if (command.prefix.equals(prefix))
				return command.execute(parameter);
		}

		return CommandException.prefixNotFound();
	}

	public static String getCompleteCommand(String commandStart) {
		if (commandStart.isEmpty())
			return "";

		for (Command command : commandList) {
			if (command.prefix.startsWith(commandStart))
				return command.prefix;
		}

		return commandStart;
	}

	public static class Actions {
		private static CommandException folder(AbstractParameter<?>[] params) {
			String text = ((StringParameter)params[0]).getValue();

			switch (text) {
				case "resources": Util.getOSType().openFile(mc.getFileResourcePacks()); break;
				case "mods": Util.getOSType().openFile(FMLPaths.MODSDIR.get().toFile()); break;
				case "mc": Util.getOSType().openFile(FMLPaths.MODSDIR.get().getParent().toFile()); break;
			}

			return null;
		}

		private static CommandException image(AbstractParameter<?>[] params) {
			String text = ((StringParameter)params[0]).getValue();

			switch (text) {
				case "trades": mc.displayGuiScreen(new ImageScreen("trades_screen", 1403, 256, 300, 256, "clientmod:textures/gui/trades_horizontal.png")); break;
				case "brewing": mc.displayGuiScreen(new ImageScreen("brewing_guide", 350, 600, 350, 256, "clientmod:textures/gui/brewing_guide.png")); break;
			}

			return null;
		}

		private static CommandException log(AbstractParameter<?>[] params) {
			String text = ((StringParameter)params[0]).getValue();

			if (text.equals("lastDeath")) {
				if (FieldHolder.lastDeathPosition == null) {
					mc.player.sendMessage(new TranslationTextComponent("messages.clientmod:log.noLastDeathPosition"), Util.DUMMY_UUID);
				}
				else {
					mc.player.sendMessage(new TranslationTextComponent("messages.clientmod:log.lastDeathPosition", ClientUtility.fancyBlockPos(FieldHolder.lastDeathPosition, mc.player.getPosition())), Util.DUMMY_UUID);
				}
			}

			return null;
		}

		private static CommandException msg(AbstractParameter<?>[] params) {
			String text = ((StringParameter)params[0]).getValue();

			if (text.equals("leave"))
				mc.player.sendChatMessage("Redstone has left the server.");
			else
				return CommandException.invalidParameter(params[0], 0, text);

			return null;
		}

		private static CommandException namemc(AbstractParameter<?>[] params) {
			String text = ((StringParameter)params[0]).getValue();
			String link = "https://de.namemc.com/profile/"+text;

			Util.getOSType().openURI(link);
			return null;
		}

		private static CommandException radar(AbstractParameter<?> [] params) {
			ClientPlayerEntity player = mc.player;
			int range = ((IntParameter)params[0]).getValue();
			EntityType<?> entity = ((EntityTypeParameter)params[1]).getValue();
			AxisAlignedBB boundingBox = player.getBoundingBox().grow(range);

			if (entity == null) {
				List<Entity> list = mc.world.getEntitiesInAABBexcluding(player, boundingBox, null);
				HashMap<Class<? extends Entity>, Integer> map = ClientUtility.countEntitiesInList(list);

				if (list.size() == 0)
					player.sendMessage(new TranslationTextComponent("messages.clientmod:radar.noEntitiesInRange", range), Util.DUMMY_UUID);
				else {
					player.sendMessage(new TranslationTextComponent("messages.clientmod:radar.entitiesInRange", range), Util.DUMMY_UUID);
					map.forEach((key, value) -> player.sendMessage(new StringTextComponent("- " + value + " " + key.getSimpleName()), Util.DUMMY_UUID));
				}
			} else {
				List<? extends Entity> list = mc.world.getEntitiesWithinAABB(entity, boundingBox, (s) -> true);

				if (list.size() == 0) {
					player.sendMessage(new TranslationTextComponent("messages.clientmod:radar.noEntityTypeInRange", new TranslationTextComponent(entity.toString()), range), Util.DUMMY_UUID);
				}
				else {
					player.sendMessage(new TranslationTextComponent("messages.clientmod:radar.entityTypeInRange", list.size(), new TranslationTextComponent(entity.toString()), range), Util.DUMMY_UUID);
					list.forEach((entry) -> player.sendMessage(new StringTextComponent("- " + entry.getName().getString() + " (" + ClientUtility.formatBlockPos(entry.getPosition()) + ")"), Util.DUMMY_UUID));
				}
			}

			return null;
		}

		private static CommandException settings(AbstractParameter<?>[] params) {
			mc.displayGuiScreen(new SettingsScreen());
			return null;
		}
		
		private static CommandException waypoint(AbstractParameter<?>[] params) {
			String text = ((StringParameter)params[0]).getValue();
			Integer x = ((IntParameter)params[1]).getValue();
			Integer y = ((IntParameter)params[2]).getValue();
			Integer z = ((IntParameter)params[3]).getValue();
			WaypointManager waypointManager = WaypointManager.getInstance();

			if (text.isEmpty()) {
				waypointManager.setWaypoint(mc.player.getPosition());
				return null;
			}
			else if (text.equals("remove")) {
				waypointManager.resetWaypoint();
				return null;
			}
			else if (text.equals("get")) {
				if (waypointManager.hasWaypoint())
					mc.player.sendMessage(new TranslationTextComponent("messages.clientmod:waypoint.currentWaypoint", ClientUtility.formatBlockPos(waypointManager.getWaypoint())), Util.DUMMY_UUID);
				else
					mc.player.sendMessage(new TranslationTextComponent("messages.clientmod:waypoint.noWaypoint"), Util.DUMMY_UUID);

				return null;

			}
			else if (text.equals("set")) {
				if (x != null && y != null && z != null) {
					BlockPos pos = new BlockPos(x, y, z);

					waypointManager.setWaypoint(pos);
					return null;
				}

				//if one of the numbers is not given, return an exception
				for (int i = 1; i < 4; i++) {
					if (params[i].getValue() == null)
						return CommandException.noParameter(params[i], i);
				}
			}

			return null;
		}

		private static CommandException wiki(AbstractParameter<?>[] params) {
			String text = ((StringParameter)params[0]).getValue().replace(" ", "_");
			String wiki_link = "https://minecraft.gamepedia.com/"+text;

			Util.getOSType().openURI(wiki_link);
			return null;
		}
	}
}
