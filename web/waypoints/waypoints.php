<?php
/*
 * Waypoints - Web Frontend - Database Interface
 * Matt Schneeberger (helluvamatt@gmail.com)
 *
 * This PHP script will access your Waypoints database and output XML that will be
 * parsed by the jQuery based JavaScript addon for Minecraft Overviewer.
 *
 * We support SQLite and MySQL databases (just like the plugin)
 * Be sure to only uncomment ONE DBMS
 *
 */
// To Use MySQL:
$db['dbms'] = "mysql";
$db['server'] = "localhost";		// MySQL Server
$db['port'] = 3306;					// MySQL Port
$db['database'] = "mcwaypoints";	// Database Name
$db['username'] = "dbuser";			// Database User
$db['password'] = "dbpass";			// Database Password

// To Use SQLite:
//$db['dbms'] = "sqlite";
//$db['file'] = "path/to/waypoints.db";

// END OF USER CONFIGURABLE SECTION

header('Content-type: text/xhtml');
echo "<waypoints>\n";

$sql = "SELECT * FROM WpWaypoints";
if ($db['dbms'] == 'mysql') {
	$conn = new mysqli($db['server'], $db['username'], $db['password'], $db['database'], $db['port']);
	if ($conn->connect_error) {
		echo "	<error>Connect Error (" . $conn->connect_errno . ") " . $conn->connect_error . "</error>\n";
	} else {
		if (($result = $conn->query($sql, MYSQLI_USE_RESULT))) {
			while ($row = $result->fetch_assoc()) {
				echo "	<waypoint>\n";
				echo "		<name>" . $row['name'] . "</name>\n";
				echo "		<description>\n";
				echo $row['description'];
				echo "\n		</description>\n";
				echo "		<owner>" . $row['owner'] . "</owner>\n";
				echo "		<location world=\"" . $row['world'] . "\" x=\"" . $row['loc_x'] . "\" y=\"" . $row['loc_y'] . "\" z=\"" . $row['loc_z'] . "\" />\n";
				echo "	</waypoint>\n";
			}
			$result->close();
		} else {
			echo "	<error>Query failed (" . $conn->connect_errno . ") " . $conn->connect_error . "</error>\n";
		}
		$conn->close();
	}

} else if ($db['dbms'] == 'sqlite') {
	$conn = new SQLite3($db['file'], SQLITE3_OPEN_READONLY);
	if (($result = $conn->query($sql))) {
		while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
			echo "	<waypoint>\n";
			echo "		<name>" . $row['name'] . "</name>\n";
			echo "		<description>\n";
			echo $row['description'];
			echo "\n		</description>\n";
			echo "		<owner>" . $row['owner'] . "</owner>\n";
			echo "		<location world=\"" . $row['world'] . "\" x=\"" . $row['loc_x'] . "\" y=\"" . $row['loc_y'] . "\" z=\"" . $row['loc_z'] . "\" />\n";
			echo "	</waypoint>\n";
		}
		$result->finalize();
	} else {
		echo "	<error>Database Error: (" . $conn->lastErrorCode . ") " . $conn->lastErrorMsg . "</error>\n";
	}
	$conn->close();
} else {
	echo "	<error>Configuration Error: Invalid DBMS.</error>\n";
}



echo "</waypoints>\n";
?>
