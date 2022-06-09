package net.redstonedubstep.clientmod.misc;

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
		player.sendSystemMessage(Component.translatable("messages.clientmod:waypoint.waypointSet", ClientUtility.formatBlockPos(waypoint)));
	}

	public BlockPos getWaypoint() {
		return waypoint;
	}

	public void resetWaypoint() {
		if (waypoint != null) {
			player.sendSystemMessage(Component.translatable("messages.clientmod:waypoint.waypointRemoved", ClientUtility.formatBlockPos(waypoint)));
			waypoint = null;
		}
		else {
			player.sendSystemMessage(Component.translatable("messages.clientmod:waypoint.noWaypoint"));
		}
	}

	public boolean hasWaypoint() {
		return waypoint != null;
	}

}
