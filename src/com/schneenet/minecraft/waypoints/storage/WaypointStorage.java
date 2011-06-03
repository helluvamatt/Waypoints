package com.schneenet.minecraft.waypoints.storage;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public interface WaypointStorage {
	
	public void init(Server server);
	
	public Waypoint find(String name) throws WaypointNotFoundException;
	public List<Waypoint> findAllByUser(Player player);
	public List<Waypoint> findAll();
	public List<Waypoint> findAllPage(int page, int size);
	
	public boolean load();
	public boolean save();
	
	public boolean add(Waypoint waypoint);
	public boolean delete(Waypoint waypoint);
	public boolean edit(Waypoint waypoint, String newDescription, Location newLoc);
	
}
