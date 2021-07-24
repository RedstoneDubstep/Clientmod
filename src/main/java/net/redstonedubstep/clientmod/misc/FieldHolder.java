package net.redstonedubstep.clientmod.misc;

import java.util.List;

import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.util.math.BlockPos;

public class FieldHolder {
	public static boolean isMinecraftStarting = true;
	public static BlockPos lastDeathPosition = null;

	//mixin related fields that can't be put in mixin classes
	public static IFutureReloadListener currentTask;
	public static List<IFutureReloadListener> oldTaskSet;
	public static int maxTaskAmount = -1;
	public static long reloadingStartTime = -1;
	public static long reloadingFinishTime = -1;
}