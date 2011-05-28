package com.schneenet.minecraft.waypoints.storage;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Waypoint {

	private String mName;
	private String mDesc;
	private Player mOwner;
	private Location mLocation;
	private World mWorld;
	
	public Waypoint(String name, String description, Player owner, World world, Location location) {
		this.mName = name;
		this.mDesc = description;
		this.mOwner = owner;
		this.mWorld = world;
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
	
	public World getWorld() {
		return this.mWorld;
	}
	
	public Location getLocation() {
		return this.mLocation;
	}
	
	public static Waypoint build(Server s, String name, String description, String owner, String world, double x, double y, double z) {
		return new Waypoint(
				name,
				description,
				s.getPlayer(owner),
				s.getWorld(world),
				new Location(s.getWorld(world), x, y, z)
				);
		
	}
	
}
