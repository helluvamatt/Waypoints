package com.schneenet.minecraft.waypoints.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.bukkit.entity.Player;
import org.sqlite.SQLiteDataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class WaypointStorageSQL implements WaypointStorage {

	private static final String SQL_WAYPOINT_ADD = "INSERT INTO ";
	private static final String SQL_WAYPOINT_DELETE = "";
	private static final String SQL_WAYPOINT_FIND_ONE = "";
	private static final String SQL_WAYPOINT_FIND_ALL = "";
	private static final String SQL_WAYPOINT_FIND_USER = "";
	
	private static final String SQL_USERS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS WpUsers (" +
			"username VARCHAR(32) NOT NULL UNIQUE" +
			")";
	private static final String SQL_WAYPOINTS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS WpWaypoints (" +
			"name VARCHAR(32) NOT NULL PRIMARY KEY UNIQUE, " +
			"owner VARCHAR(32) NOT NULL, " +
			"description TEXT DEFAULT NULL, " +
			"loc_x FLOAT NOT NULL, " +
			"loc_y FLOAT NOT NULL, " +
			"loc_z FLOAT NOT NULL" +
			")";
	
	private static Dbms dbms;
	private static DataSource dbSource;
	private static Connection dbConn;
	private static Logger logger;

	@Override
	public void add(Waypoint waypoint) {
		try {
			PreparedStatement ps = dbConn.prepareStatement(SQL_WAYPOINT_ADD);
			
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "[WaypointStorageSQL] " + e.getMessage(), e);
		}

	}

	@Override
	public void delete(Waypoint waypoint) {
		// TODO Auto-generated method stub

	}

	@Override
	public Waypoint find(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Waypoint> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Waypoint> findAllByUser(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean load() {
		// Do nothing, find** functions operate directly on the Database
		return true;
	}

	@Override
	public boolean save() {
		// Don't do anything, changes are saved immediately upon adding or
		// deleting
		return true;
	}
	
	public void init(String dbmsName, String uri, String username, String password, Logger log) throws Exception {
		try {
			dbms = Dbms.valueOf(dbmsName);
		} catch (IllegalArgumentException e) {
			dbms = Dbms.SQLITE;
		}
		try {
			Class.forName(dbms.getDriver());
		} catch (ClassNotFoundException e) {
			throw new Exception("Unable to load SQL Driver!", e);
		}
		dbSource = dbms.getDataSource(username, password, uri);
		dbConn = dbSource.getConnection();
		logger = log;
	}

	public static enum Dbms {
		SQLITE("org.sqlite.JDBC"), MYSQL("com.mysql.jdbc.Driver");
		private final String driver;

		Dbms(String driverClass) {
			this.driver = driverClass;
		}

		public String getDriver() {
			return driver;
		}

		public DataSource getDataSource(String username, String password, String url) {
			switch (this) {
			case MYSQL:
				MysqlDataSource mds = new MysqlDataSource();
				mds.setUser(username);
				mds.setPassword(password);
				mds.setUrl(url);
				return mds;
			default:
			case SQLITE:
				SQLiteDataSource sds = new SQLiteDataSource();
				sds.setUrl(url);
				return sds;
			}
		}
	}

}
