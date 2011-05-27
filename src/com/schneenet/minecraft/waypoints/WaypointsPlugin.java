package com.schneenet.minecraft.waypoints;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class WaypointsPlugin extends JavaPlugin {

	private static final String COMMAND_WAYPOINT = "waypoint";
	private static final String COMMAND_WAYPOINT_DELETE = "delete";
	private static final String COMMAND_WAYPOINT_CREATE = "create";

	private static Logger logger = Logger.getLogger("minecraft");
	private static PermissionHandler handler;
	public static String name;
	public static String version;

	@Override
	public void onDisable() {
		logger.info(name + " v" + version + " shut down successfully.");
	}

	@Override
	public void onEnable() {

		PluginDescriptionFile pdFile = this.getDescription();
		name = pdFile.getName();
		version = pdFile.getVersion();
		
		this.setupPermissions();

		WaypointsBlockListener waypointsBlockListener = new WaypointsBlockListener(this);
		this.getServer().getPluginManager().registerEvent(Event.Type.SIGN_CHANGE, waypointsBlockListener, Event.Priority.Normal, this);

		logger.info(name + " v" + version + " is enabled.");
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String commandName = command.getName();
		if (COMMAND_WAYPOINT.compareToIgnoreCase(commandName) == 0) {
			
			Player player = null;
			if (sender instanceof Player) {
				player = (Player) sender;
			} else {
				sender.sendMessage(ChatColor.RED + "Command can only be used by players!");
				return true;
			}
			
			if (args.length > 1) {
				
				String name = args[1];
				String desc = "";
				
				if (args.length > 2) {
					desc = args[2];
				}
				
				// TODO Continue parsing command arguments to create or delete a waypoint based on the player's current location.
				if (COMMAND_WAYPOINT_DELETE.compareToIgnoreCase(args[0]) == 0) {
					// Waypoint delete
					// 1. Check if the waypoint exists (SQL: SELECT FOR UPDATE)
					// 2. Check if the owner is our current user (or has 'waypoint.admin' permission
					// 3. Delete the waypoint from the database
					
					// TODO Check permission for admin commands: delete unowned (more?)
					
				} else if (COMMAND_WAYPOINT_CREATE.compareToIgnoreCase(args[0]) == 0) {
					// Check permission for basic creation
					if (handler.has(player, "waypoints.create")) {
						// Waypoint create
						// 1. Insert waypoint record into database.
					} else {
						player.sendMessage(ChatColor.RED + "You do not have the proper permission to do that!");
					}
				} else {
					player.sendMessage(ChatColor.RED + "Usage: /waypoint create|delete <name> [<description>]");
				}
			} else {
				player.sendMessage(ChatColor.RED + "Usage: /waypoint create|delete <name> [<description>]");
			}
			return true;
		}
		return false;
	}

	public static PermissionHandler getPermissionHandler() {
		return handler;
	}

	private void setupPermissions() {
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
		if (handler == null) {
			if (permissionsPlugin != null) {

				handler = ((Permissions) permissionsPlugin).getHandler();
				logger.info("Permissions " + Permissions.version + " (" + Permissions.codename + ") found. Using it for permissions.");
			}

		}
	}

}
