package net.redstonedubstep.clientmod.misc;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

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
		return "x"+x+", y"+y+", z"+z;
	}
}
