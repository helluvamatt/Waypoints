package com.schneenet.minecraft.waypoints;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.schneenet.minecraft.waypoints.storage.Waypoint;
import com.schneenet.minecraft.waypoints.storage.WaypointNotFoundException;
import com.schneenet.minecraft.waypoints.storage.WaypointStorage;
import com.schneenet.minecraft.waypoints.storage.WaypointStorageSQL;
import com.schneenet.minecraft.waypoints.storage.WaypointStorageSQL.Dbms;

public class WaypointsPlugin extends JavaPlugin {

	public static final int DEFAULT_PAGE_SIZE = 7;
	private static final String COMMAND_WAYPOINT = "waypoint";
	private static final String COMMAND_LIST = "wplist";
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
		storage.init(this.getServer());
		try {
			((WaypointStorageSQL) storage).initSql(dbmsName, uri, username, password);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "[" + name + "] Exception while setting up SQL Storage:", e);
		}
		storage.load();

		//WaypointsBlockListener waypointsBlockListener = new WaypointsBlockListener(this);
		//this.getServer().getPluginManager().registerEvent(Event.Type.SIGN_CHANGE, waypointsBlockListener, Event.Priority.Normal, this);

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
				// Continue parsing command arguments to create or delete a
				// waypoint based on the player's current location.
				if (COMMAND_WAYPOINT_DELETE.compareToIgnoreCase(args[0]) == 0) {
					// Waypoint delete
					// 1. Check if the waypoint exists
					Waypoint waypoint;
					try {
						waypoint = storage.find(name);
						// 2. Check if the owner is our current user (or has
						// 'waypoint.admin' permission
						if (player.getName().compareTo(waypoint.getOwner().getName()) == 1 || handler.has(player, "waypoints.admin.delete")) {
							// 3. Delete the waypoint from the database
							storage.delete(waypoint);
						} else {
							player.sendMessage(ChatColor.RED + "You do not have the proper permission to do that!");
						}
					} catch (WaypointNotFoundException e) {
						player.sendMessage(ChatColor.RED + "There is no waypoint by that name.");
					}
				} else if (COMMAND_WAYPOINT_CREATE.compareToIgnoreCase(args[0]) == 0) {
					// Check permission for basic creation
					if (handler.has(player, "waypoints.create")) {
						Waypoint waypoint = new Waypoint(name, desc, player, player.getWorld(), player.getLocation());
						storage.add(waypoint);
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
		} else if (COMMAND_LIST.compareToIgnoreCase(commandName) == 0) {
			int page = 1;
			int size = DEFAULT_PAGE_SIZE;
			if (args.length > 0) {
				page = Integer.parseInt(args[0]);
				if (args.length > 1) {
					size = Integer.parseInt(args[1]);
				}
			}
			List<Waypoint> list = storage.findAllPage(page, size);
			sender.sendMessage(ChatColor.DARK_GREEN + "Waypoints" + ChatColor.WHITE + " -------------------");
			if (!list.isEmpty()) {
				Iterator<Waypoint> iter = list.iterator();
				while (iter.hasNext()) {
					Waypoint waypoint = iter.next();
					String formatStr =
						ChatColor.GREEN + "%-16s" + ChatColor.WHITE + " (" +
						ChatColor.AQUA + "%6.1f" + ChatColor.WHITE + " ," +
						ChatColor.AQUA + "%6.1f" + ChatColor.WHITE + " ," +
						ChatColor.AQUA + "%6.1f" + ChatColor.WHITE + ")";
					sender.sendMessage(String.format(formatStr, waypoint.getName(), waypoint.getLocation().getX(), waypoint.getLocation().getY(), waypoint.getLocation().getZ()));
				}
			} else {
				sender.sendMessage(ChatColor.RED + "There are no waypoints to display.");
			}
			return true;
		}
		return false;
	}

	public static PermissionHandler getPermissionHandler() {
		return handler;
	}
	
	public WaypointStorage getWaypointStorage() {
		return this.storage;
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
