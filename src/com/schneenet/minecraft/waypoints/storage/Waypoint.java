package com.schneenet.minecraft.waypoints.storage;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
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
	
	/**
	 * Warp the specified player to this waypoint
	 * @param p Player
	 */
	public void warp(Player p) {
		Chunk chunk = this.mWorld.getChunkAt(this.mLocation);
		if (mWorld.isChunkLoaded(chunk)) {
			mWorld.loadChunk(chunk);
		}
		p.teleport(this.mLocation);
		p.sendMessage(ChatColor.GREEN + "This is '" + ChatColor.AQUA + this.mName + ChatColor.GREEN + "'.");
	}
	
	/**
	 * Build a waypoint from basic datatypes
	 * @param s Server
	 * @param name Name of waypoint
	 * @param description Description of waypoint
	 * @param owner Owner's username
	 * @param world World name
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 * @return Waypoint object
	 */
	public static Waypoint build(Server s, String name, String description, String owner, String world, double x, double y, double z) {
		return new Waypoint(
				name,
				description,
				s.getPlayer(owner),
				s.getWorld(world),
				new Location(s.getWorld(world), x, y, z)
				);
		
	}
	
	/**
	 * Concatenate the rest of the arguments into a single space-delimited string starting at start
	 * @param args Array of Strings to be concatenated
	 * @param start Start at this element
	 * @return Concatenated string
	 */
	public static String buildString(String[] args, int start) {
		StringBuilder wpName = new StringBuilder();
		for (int i = start; i < args.length; i++) {
			wpName.append(args[i]);
			if (i < (args.length - 1)) {
				wpName.append(' ');
			}
		}
		return wpName.toString();
	}
	
}
