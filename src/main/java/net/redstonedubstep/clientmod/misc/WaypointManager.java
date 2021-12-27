package net.redstonedubstep.clientmod.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;

public class WaypointManager {
	private static WaypointManager instance;
	private BlockPos waypoint;
	private PlayerEntity player = Minecraft.getInstance().player;

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
		player.sendMessage(new TranslationTextComponent("messages.clientmod:waypoint.waypointSet", ClientUtility.formatBlockPos(waypoint)), Util.NIL_UUID);
	}

	public BlockPos getWaypoint() {
		return waypoint;
	}

	public void resetWaypoint() {
		if (waypoint != null) {
			player.sendMessage(new TranslationTextComponent("messages.clientmod:waypoint.waypointRemoved", ClientUtility.formatBlockPos(waypoint)), Util.NIL_UUID);
			waypoint = null;
		}
		else {
			player.sendMessage(new TranslationTextComponent("messages.clientmod:waypoint.noWaypoint"), Util.NIL_UUID);
		}
	}

	public boolean hasWaypoint() {
		return waypoint != null;
	}

}
