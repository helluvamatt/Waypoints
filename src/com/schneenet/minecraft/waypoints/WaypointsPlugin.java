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
		Player player = null;
		String commandName = command.getName();

		if (sender instanceof Player) {
			player = (Player) sender;
		} else {
			sender.sendMessage(ChatColor.RED + "Command can only be used by players!");
			return false;
		}
		
		if (COMMAND_WAYPOINT.compareToIgnoreCase(commandName) == 0) {
			if (args.length > 2) {
				
				// TODO Continue parsing command arguments to create or delete a waypoint based on the player's current location.
				
				// TODO Check permission for admin commands: delete unowned (more?)
				
				// Check permission for basic creation
				if (handler.has(player, "waypoints.create")) {
					
				} else {
					player.sendMessage(ChatColor.RED + "You do not have the proper permission to do that!");
				}
				
			} else {
				player.sendMessage(ChatColor.RED + "Usage: /waypoint create|delete <name> [<description>]");
			}
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
