package net.redstonedubstep.clientmod.command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.client.resources.PaintingTextureManager;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLPaths;
import net.redstonedubstep.clientmod.Clientmod;
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
	private static final Command RAY_COMMAND = new Command("ray", CommandLibrary.Actions::ray, new IntParameter(false, 100, 10000, 0), new StringParameter(Lists.newArrayList("all", "entity", "block"), false, "all"));
	private static final Command RELOAD_COMMAND = new Command("reload", CommandLibrary.Actions::reload, new StringParameter(Lists.newArrayList("all", "font", "misc", "renderers", "sounds", "textures"), false, "all"));
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
		commandList.add(RAY_COMMAND);
		commandList.add(RELOAD_COMMAND);
		commandList.add(SETTINGS_COMMAND);
		commandList.add(WAYPOINT_COMMAND);
		commandList.add(WIKI_COMMAND);
	}

	public static CommandException parseAndExecuteCommand(String unparsedCommand) {
		String[] splitCommand = unparsedCommand.split(" ", 2);
		String prefix = splitCommand[0];
		String parameter = splitCommand.length > 1 ? splitCommand[1] : "";

		for (Command command : commandList) {
			if (command.prefix.equals(prefix))
				return command.execute(parameter);
		}

		return CommandException.prefixNotFound(prefix);
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
				case "resources": Util.getPlatform().openFile(mc.getResourcePackDirectory()); break;
				case "mods": Util.getPlatform().openFile(FMLPaths.MODSDIR.get().toFile()); break;
				case "mc": Util.getPlatform().openFile(FMLPaths.MODSDIR.get().getParent().toFile()); break;
			}

			return null;
		}

		private static CommandException image(AbstractParameter<?>[] params) {
			String text = ((StringParameter)params[0]).getValue();

			switch(text) {
				case "trades" -> mc.setScreen(new ImageScreen("trades_screen", 1263, 595, new ResourceLocation(Clientmod.MODID, "textures/gui/trading_bartering_guide.png")));
				case "brewing" -> mc.setScreen(new ImageScreen("brewing_guide", 350, 600, new ResourceLocation(Clientmod.MODID, "textures/gui/brewing_guide.png")));
			}

			return null;
		}

		private static CommandException log(AbstractParameter<?>[] params) {
			String text = ((StringParameter)params[0]).getValue();

			if (text.equals("lastDeath")) {
				if (FieldHolder.lastDeathPosition == null) {
					mc.player.sendSystemMessage(Component.translatable("messages.clientmod:log.noLastDeathPosition"));
				}
				else {
					mc.player.sendSystemMessage(Component.translatable("messages.clientmod:log.lastDeathPosition", ClientUtility.fancyWaypointBlockPos(FieldHolder.lastDeathPosition, mc.player.blockPosition())));
				}
			}

			return null;
		}

		private static CommandException msg(AbstractParameter<?>[] params) {
			String text = ((StringParameter)params[0]).getValue();

			if (text.equals("leave"))
				mc.player.chat("Redstone has left the server.");
			else
				return CommandException.invalidParameter(params[0], 0, text);

			return null;
		}

		private static CommandException namemc(AbstractParameter<?>[] params) {
			String text = ((StringParameter)params[0]).getValue();
			String link = "https://de.namemc.com/profile/"+text;

			Util.getPlatform().openUri(link);
			return null;
		}

		private static CommandException radar(AbstractParameter<?> [] params) {
			LocalPlayer player = mc.player;
			int range = ((IntParameter)params[0]).getValue();
			EntityType<?> entity = ((EntityTypeParameter)params[1]).getValue();
			AABB boundingBox = player.getBoundingBox().inflate(range);

			if (entity == null) {
				List<Entity> list = mc.level.getEntities(player, boundingBox, s -> true);
				HashMap<Class<? extends Entity>, Integer> map = ClientUtility.countEntitiesInList(list);

				if (list.size() == 0)
					player.sendSystemMessage(Component.translatable("messages.clientmod:radar.noEntitiesInRange", range));
				else {
					player.sendSystemMessage(Component.translatable("messages.clientmod:radar.entitiesInRange", range));
					map.forEach((key, value) -> player.sendSystemMessage(Component.literal("- " + value + " " + key.getSimpleName())));
				}
			} else {
				List<? extends Entity> list = mc.level.getEntities(entity, boundingBox, s -> true);
				list.sort(Comparator.comparingDouble(e -> ClientUtility.distanceBetween(e.blockPosition(), mc.player.blockPosition())));

				if (list.size() == 0) {
					player.sendSystemMessage(Component.translatable("messages.clientmod:radar.noEntityTypeInRange", Component.translatable(entity.toString()), range));
				}
				else {
					player.sendSystemMessage(Component.translatable("messages.clientmod:radar.entityTypeInRange", list.size(), Component.translatable(entity.toString()), range));
					list.forEach((entry) -> player.sendSystemMessage(Component.literal("- " + entry.getName().getString() + " (").append(ClientUtility.fancyWaypointBlockPos(entry.blockPosition(), mc.player.blockPosition())).append(")")));
				}
			}

			return null;
		}

		private static CommandException ray(AbstractParameter<?>[] params) {
			LocalPlayer player = mc.player;
			int range = ((IntParameter)params[0]).getValue();
			String type = ((StringParameter)params[1]).getValue();
			Vec3 playerEyePos = player.getEyePosition();
			Vec3 lookVector = player.getLookAngle();
			Vec3 endPos = playerEyePos.add(lookVector.scale(range));

			if (type.equals("entity")) {
				EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(player, playerEyePos, endPos, player.getBoundingBox().inflate(range), e -> true, range * range);

				if (entityHitResult != null) {
					String distance = String.format(Locale.ROOT, "%.3f", entityHitResult.getLocation().subtract(playerEyePos).length());

					player.sendSystemMessage(Component.translatable("messages.clientmod:ray.entity", entityHitResult.getEntity().getName(), ClientUtility.formatBlockPos(new BlockPos(entityHitResult.getLocation())), distance));
					return null;
				}
			}
			else if (type.equals("block") || type.equals("all") || type.isEmpty()) {
				BlockHitResult hitResult = mc.level.clip(new ClipContext(playerEyePos, endPos, Block.OUTLINE, type.equals("block") ? Fluid.NONE : Fluid.ANY, player));

				if (hitResult.getType() != Type.MISS) {
					BlockState state = mc.level.getBlockState(hitResult.getBlockPos());
					String distance = String.format(Locale.ROOT, "%.3f", hitResult.getLocation().subtract(playerEyePos).length());

					player.sendSystemMessage(Component.translatable("messages.clientmod:ray.block", state.getBlock().getName(), ClientUtility.formatBlockPos(hitResult.getBlockPos()), distance));
					return null;
				}
			}

			player.sendSystemMessage(Component.translatable("messages.clientmod:ray.miss", range));
			return null;
		}

		private static CommandException reload(AbstractParameter<?>[] params) {
			String text = ((StringParameter)params[0]).getValue().replace(" ", "_");

			FieldHolder.reloadFilter = switch(text) {
				default -> null;
				case "font" -> Sets.newHashSet(LanguageManager.class, SplashManager.class, FontManager.class);
				case "misc" -> Sets.newHashSet(GrassColorReloadListener.class, FoliageColorReloadListener.class, SearchRegistry.class);
				case "renderers" -> Sets.newHashSet(BlockEntityRenderDispatcher.class, BlockEntityWithoutLevelRenderer.class, EntityRenderDispatcher.class, ItemInHandRenderer.class, GameRenderer.class, LevelRenderer.class);
				case "sounds" -> Sets.newHashSet(SoundManager.class);
				case "textures" -> Sets.newHashSet(TextureManager.class, ModelManager.class, EntityModelSet.class, BlockRenderDispatcher.class, PaintingTextureManager.class, MobEffectTextureManager.class);
			};

			mc.reloadResourcePacks();
			return null;
		}

		private static CommandException settings(AbstractParameter<?>[] params) {
			mc.setScreen(new SettingsScreen());
			return null;
		}
		
		private static CommandException waypoint(AbstractParameter<?>[] params) {
			String text = ((StringParameter)params[0]).getValue();
			Integer x = ((IntParameter)params[1]).getValue();
			Integer y = ((IntParameter)params[2]).getValue();
			Integer z = ((IntParameter)params[3]).getValue();
			WaypointManager waypointManager = WaypointManager.getInstance();

			if (text.isEmpty()) {
				waypointManager.setWaypoint(mc.player.blockPosition());
				return null;
			}
			else if (text.equals("remove")) {
				waypointManager.resetWaypoint();
				return null;
			}
			else if (text.equals("get")) {
				if (waypointManager.hasWaypoint())
					mc.player.sendSystemMessage(Component.translatable("messages.clientmod:waypoint.currentWaypoint", ClientUtility.formatBlockPos(waypointManager.getWaypoint())));
				else
					mc.player.sendSystemMessage(Component.translatable("messages.clientmod:waypoint.noWaypoint"));

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
			String wiki_link = "https://minecraft.fandom.com/" + text;

			Util.getPlatform().openUri(wiki_link);
			return null;
		}
	}
}
