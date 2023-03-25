package net.redstonedubstep.clientmod.misc;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.entity.EntityType;

public class FieldHolder {
	public static boolean isMinecraftStarting = true;
	public static BlockPos lastDeathPosition = null;
	public static Collection<Class<?>> reloadFilter = null;
	public static final Set<ResourceLocation> renderableBlockEntityFilter = new HashSet<>();
	public static final Set<EntityType<?>> renderableEntityFilter = new HashSet<>();

	//mixin related fields that can't be put in mixin classes TODO: put these in a mixin class with protected and @Unique
	public static PreparableReloadListener currentTask;
	public static List<PreparableReloadListener> oldTaskSet;
	public static int maxTaskAmount = -1;
	public static long reloadingStartTime = -1;
	public static long reloadingFinishTime = -1;
	public static boolean wasReloadingDone = false;
}