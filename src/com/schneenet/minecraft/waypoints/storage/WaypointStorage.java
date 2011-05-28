package com.schneenet.minecraft.waypoints.storage;

import java.util.List;

import org.bukkit.entity.Player;

public interface WaypointStorage {
	
	public Waypoint find(String name);
	public List<Waypoint> findAllByUser(Player player);
	public List<Waypoint> findAll();
	
	public boolean load();
	public boolean save();
	
	public void add(Waypoint waypoint);
	public void delete(Waypoint waypoint);
	
}
