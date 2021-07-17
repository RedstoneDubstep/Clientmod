package net.redstonedubstep.clientmod.misc;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.event.HoverEvent.Action;

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

	public static IFormattableTextComponent fancyBlockPos(BlockPos pos, BlockPos originalPos) {
		StringTextComponent position = new StringTextComponent(formatBlockPos(pos));
		int diffX = pos.getX() - originalPos.getX();
		int diffZ = pos.getZ() - originalPos.getZ();
		String direction;

		if (diffZ > 0) {
			direction = diffX > 0 ? "Southeast" : "Southwest";
		}
		else {
			direction = diffX > 0 ? "Northeast" : "Northwest";
		}

		position.modifyStyle((s) -> s.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new StringTextComponent(direction))));
		return position;
	}

	public static ITextComponent fancyWaypointBlockPos(BlockPos pos, BlockPos originalPos) {
		IFormattableTextComponent fancyBlockPos = fancyBlockPos(pos, originalPos);
		String clickCommand = "/clientmod waypoint set " + pos.getX() + " " + pos.getY() + " " + pos.getZ();

		fancyBlockPos.modifyStyle(s -> s.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickCommand)));
		return fancyBlockPos;
	}

	public static double distanceBetween(BlockPos pos1, BlockPos pos2) {
		return Vector3d.copy(pos1.subtract(pos2)).length();
	}
}
