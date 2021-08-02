package net.redstonedubstep.clientmod.misc;

import java.util.List;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.core.BlockPos;

public class FieldHolder {
	public static boolean isMinecraftStarting = true;
	public static BlockPos lastDeathPosition = null;

	//mixin related fields that can't be put in mixin classes
	public static PreparableReloadListener currentTask;
	public static List<PreparableReloadListener> oldTaskSet;
	public static int maxTaskAmount = -1;
	public static long reloadingStartTime = -1;
	public static long reloadingFinishTime = -1;
}