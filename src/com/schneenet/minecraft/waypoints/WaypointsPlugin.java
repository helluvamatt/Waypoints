package com.schneenet.minecraft.waypoints;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.schneenet.minecraft.waypoints.storage.WaypointStorage;
import com.schneenet.minecraft.waypoints.storage.WaypointStorageSQL;
import com.schneenet.minecraft.waypoints.storage.WaypointStorageSQL.Dbms;

public class WaypointsPlugin extends JavaPlugin {

	private static final String COMMAND_WAYPOINT = "waypoint";
	private static final String COMMAND_WAYPOINT_DELETE = "delete";
	private static final String COMMAND_WAYPOINT_CREATE = "create";

	//private static final String STORAGE_ENGINE_TYPE_SQL = "sql";
	//private static final String STORAGE_ENGINE_TYPE_FILE = "file";

	private static Logger logger = Logger.getLogger("minecraft");
	private static PermissionHandler handler;
	public static String name;
	public static String version;

	private WaypointStorage storage;

	@Override
	public void onDisable() {
		if (storage != null)
			storage.save();
		logger.info(name + " v" + version + " shut down successfully.");
	}

	@Override
	public void onEnable() {

		PluginDescriptionFile pdFile = this.getDescription();
		name = pdFile.getName();
		version = pdFile.getVersion();

		this.setupPermissions();

		Configuration config = this.getConfiguration();
		//String storageEngine = config.getString("storage.engine.type", STORAGE_ENGINE_TYPE_SQL);
		String dbmsName = config.getString("storage.engine.dbms", Dbms.SQLITE.getDriver());
		String uri = config.getString("storage.engine.uri", "jdbc:sqlite:plugins/Waypoints/waypoints.db");
		String username = config.getString("storage.engine.username", "");
		String password = config.getString("storage.engine.password", "");

		// Prepare the storage engine
		// TODO Check the storage.engine.type in the config and also do a flat file storage engine
		storage = new WaypointStorageSQL();
		try {
			((WaypointStorageSQL) storage).init(dbmsName, uri, username, password, logger);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "[" + name + "] " + e.getMessage(), e);
		}
		storage.load();

		WaypointsBlockListener waypointsBlockListener = new WaypointsBlockListener(this);
		this.getServer().getPluginManager().registerEvent(Event.Type.SIGN_CHANGE, waypointsBlockListener, Event.Priority.Normal, this);

		logger.info("[" + name + "] v" + version + " is enabled.");
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

				// TODO Continue parsing command arguments to create or delete a
				// waypoint based on the player's current location.
				if (COMMAND_WAYPOINT_DELETE.compareToIgnoreCase(args[0]) == 0) {
					// Waypoint delete
					// 1. Check if the waypoint exists (SQL: SELECT FOR UPDATE)
					// 2. Check if the owner is our current user (or has
					// 'waypoint.admin' permission
					// 3. Delete the waypoint from the database

					// TODO Check permission for admin commands: delete unowned
					// (more?)

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
