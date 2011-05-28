package com.schneenet.minecraft.waypoints.storage;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Waypoint {

	private String mName;
	private String mDesc;
	private Player mOwner;
	private Location mLocation;
	
	public Waypoint(String name, String description, Player owner, Location location) {
		this.mName = name;
		this.mDesc = description;
		this.mOwner = owner;
		this.mLocation = location;
	}
	
	public String getName() {
		return this.mName;
	}
	
	public String getDescription() {
		return this.mDesc;
	}
	
	public Player getOwner() {
		return this.mOwner;
	}
	
	public Location getLocation() {
		return this.mLocation;
	}
	
	
	
}
