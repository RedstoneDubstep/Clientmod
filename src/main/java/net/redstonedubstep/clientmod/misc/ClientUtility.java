package net.redstonedubstep.clientmod.misc;

import java.util.HashMap;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ClientUtility {
	public static HashMap<Class<? extends Entity>, Integer> countEntitiesInList(List<Entity> list) {
		HashMap<Class<? extends Entity>, Integer> map = new HashMap<>();

		for (Entity entity : list) {
			if (!map.containsKey(entity.getClass()))
				map.put(entity.getClass(), 1);
			else
				map.put(entity.getClass(), map.get(entity.getClass())+1);
		}

		return map;
	}

	public static String formatBlockPos(BlockPos pos) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		return "x" + x + ", y" + y + ", z" + z;
	}

	public static MutableComponent fancyBlockPos(BlockPos pos, BlockPos originalPos) {
		MutableComponent position = Component.literal(formatBlockPos(pos));
		int diffX = pos.getX() - originalPos.getX();
		int diffZ = pos.getZ() - originalPos.getZ();
		String direction;

		if (diffZ > 0) {
			direction = diffX > 0 ? "Southeast" : "Southwest";
		}
		else {
			direction = diffX > 0 ? "Northeast" : "Northwest";
		}

		position.withStyle((s) -> s.withHoverEvent(new HoverEvent(Action.SHOW_TEXT, Component.literal(direction))));
		return position;
	}

	public static Component fancyWaypointBlockPos(BlockPos pos, BlockPos originalPos) {
		MutableComponent fancyBlockPos = fancyBlockPos(pos, originalPos);
		String clickCommand = "/clientmod waypoint set " + pos.getX() + " " + pos.getY() + " " + pos.getZ();

		fancyBlockPos.withStyle(s -> s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickCommand)));
		return fancyBlockPos;
	}

	public static double distanceBetween(BlockPos pos1, BlockPos pos2) {
		return Vec3.atLowerCornerOf(pos1.subtract(pos2)).length();
	}
}
