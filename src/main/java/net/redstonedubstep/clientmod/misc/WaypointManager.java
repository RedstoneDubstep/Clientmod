package net.redstonedubstep.clientmod.misc;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class WaypointManager {
	private static WaypointManager instance;
	private BlockPos waypoint;
	private Player player = Minecraft.getInstance().player;

	public static WaypointManager getInstance() {
		if (instance == null) {
			instance = new WaypointManager();
		}

		return instance;
	}

	private WaypointManager() {
		this.waypoint = null;
	}

	public void setWaypoint(BlockPos waypoint) {
		this.waypoint = waypoint;
		player.sendMessage(Component.translatable("messages.clientmod:waypoint.waypointSet", ClientUtility.formatBlockPos(waypoint)), Util.NIL_UUID);
	}

	public BlockPos getWaypoint() {
		return waypoint;
	}

	public void resetWaypoint() {
		if (waypoint != null) {
			player.sendMessage(Component.translatable("messages.clientmod:waypoint.waypointRemoved", ClientUtility.formatBlockPos(waypoint)), Util.NIL_UUID);
			waypoint = null;
		}
		else {
			player.sendMessage(Component.translatable("messages.clientmod:waypoint.noWaypoint"), Util.NIL_UUID);
		}
	}

	public boolean hasWaypoint() {
		return waypoint != null;
	}

}
