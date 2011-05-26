package com.schneenet.minecraft.waypoints;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

public class WaypointsBlockListener extends BlockListener {
	
	private WaypointsPlugin plugin;
	
	public WaypointsBlockListener(WaypointsPlugin p) {
		this.plugin = p;
	}
	
	public void onSignChange(SignChangeEvent e) {
		Player player = e.getPlayer();
		if (WaypointsPlugin.getPermissionHandler().has(player, "waypoints.sign")) {
			// TODO Check the sign: "[waypoint]" (Case Insensitive) on the top line, second line is name, third and fourth lines are (optionally) the description
		}
	}
}
