package net.redstonedubstep.clientmod.misc;

import java.util.List;

import net.minecraft.resources.IFutureReloadListener;

public class MixinFieldStorage {
	public static IFutureReloadListener currentTask;
	public static List<IFutureReloadListener> oldTaskSet;
}