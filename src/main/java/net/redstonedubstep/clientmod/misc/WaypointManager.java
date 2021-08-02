package net.redstonedubstep.clientmod.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;

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
		player.sendMessage(new TranslatableComponent("messages.clientmod:waypoint.waypointSet", ClientUtility.formatBlockPos(waypoint)), Util.NIL_UUID);
	}

	public BlockPos getWaypoint() {
		return waypoint;
	}

	public void resetWaypoint() {
		if (waypoint != null) {
			player.sendMessage(new TranslatableComponent("messages.clientmod:waypoint.waypointRemoved", ClientUtility.formatBlockPos(waypoint)), Util.NIL_UUID);
			waypoint = null;
		}
		else {
			player.sendMessage(new TranslatableComponent("messages.clientmod:waypoint.noWaypoint"), Util.NIL_UUID);
		}
	}

	public boolean hasWaypoint() {
		return waypoint != null;
	}

}
