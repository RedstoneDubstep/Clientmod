package net.redstonedubstep.clientmod.misc;

import java.util.List;

import net.minecraft.resources.IFutureReloadListener;

public class FieldHolder {
	public static boolean isMinecraftStarting = true;

	//mixin related fields that can't be put in mixin classes
	public static IFutureReloadListener currentTask;
	public static List<IFutureReloadListener> oldTaskSet;
}