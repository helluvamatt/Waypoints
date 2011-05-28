package com.schneenet.minecraft.waypoints.storage;

import java.util.List;

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
	
	public void add(Waypoint waypoint);
	public void delete(Waypoint waypoint);
	
}
