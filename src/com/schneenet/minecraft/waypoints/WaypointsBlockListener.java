package com.schneenet.minecraft.waypoints;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

public class WaypointsBlockListener extends BlockListener {
	
	private WaypointsPlugin plugin;
	
	public static final String WAYPOINT_SIGN = "[WAYPOINT]";
	
	public WaypointsBlockListener(WaypointsPlugin p) {
		this.plugin = p;
	}
	
	public void onSignChange(SignChangeEvent e) {
		Player player = e.getPlayer();
		if (WaypointsPlugin.getPermissionHandler().has(player, "waypoints.sign")) {
			// Check the sign: "[waypoint]" (Case Insensitive) on the top line, second line is name, third and fourth lines are (optionally) the description
			if (e.getLine(0).trim().compareToIgnoreCase(WAYPOINT_SIGN) == 0) {
				String name = e.getLine(1);
				String desc = e.getLine(2) + e.getLine(3);
				Location loc = e.getBlock().getLocation();
				// TODO Make a new waypoint
				e.setLine(0, WAYPOINT_SIGN);
			}
		}
	}
}
