## Waypoints

Create points of interest (POIs) with signs or a command in game, teleport back to them, and show them off on Minecraft Overviewer!

<http://minecraft.schneenet.com>

<http://forums.bukkit.org/threads/tp-web-waypoints-v0-9-create-pois-to-add-to-minecraft-overviewer-818.19698/>

#### Features

*	Create POIs (Points of interest) with a single command
*	Add a description to a POI
*	Move a POI
*	Teleport to a POI
*	Show POIs on a Minecraft Overviewer map.
*	MySQL or SQLite storage options
*	Names and descriptions can have spaces.

#### I Want It

1.	Get the Dependencies:
	* Permissions 2.7.4 (Pheonix)
	* [mysql-connector-j](http://dev.mysql.com/downloads/connector/j/5.1.html) (For MySQL storage)
	* [sqlite-jdbc](http://www.xerial.org/trac/Xerial/wiki/SQLiteJDBC) (For SQLite storage, get the latest version, I believe it's 3.7.2)
	NOTE: You don't need the SQLite library if you are using MySQL and vice versa.

2.	[Download the ZIP](http://minecraft.schneenet.com/waypoints.zip) containing all the stuff you need.
3.	Unzip to a working directory. (Desktop, home directory, etc.)
4.	Move the 'Waypoints.jar' file and the 'Waypoints' folder to your '%BUKKIT_HOME%/plugins' directory.
5.	Edit '%BUKKIT_HOME%/plugins/Waypoints/config.yml' (See Configuration below)
6.	Reload CraftBukkit

#### I Want Markers on Minecraft Overviewer!

Okay, just a few more steps (using FabianN's fork of Overviewer):

1.	The ZIP file contains a folder called 'web', move the contents of that folder to the 'web_assets' folder of your Minecraft Overviewer installation.
2.	Open the 'index.html' page in web_assets and add this after the other scripts:
     `<!--Waypoints--><script type="text/javascript" src="waypoints/waypoints.js"></script>`
3.	Optionally, to style Info Windows, add this after the other `<link>` tags: `<link rel="stylesheet" href="waypoints/waypoints.css" type="text/css" />`.
4.	Open the 'overviewer.js' script in web_assets and add this: `waypoints.init();` after this (on about line 41): `overviewer.util.createMapControls();`
	Examples (ChatterCraft is also added): [index.html](http://minecraft.schneenet.com/index.html) (View source) and [overviewer.js](http://minecraft.schneenet.com/overviewer.js)

#### Commands

/waypoint create <name> | Create a new waypoint (and select it)
/waypoint select <name> | Select an existing waypoint
/waypoint deselect | Select no waypoint (deselect)
/waypoint delete | Delete the selected waypoint
/waypoint describe <description> | Describe the selected waypoint
/waypoint move | Move the waypoint to the player's location
/waypoint goto | Teleport to the selected waypoint
/waypoint list [<page> [<count>]] | List the waypoints on the server
/waypoint <name> | Teleport to the specified waypoint

#### Configuration

For MySQL storage: (plugins/Waypoints/config.yml)
`storage:
    engine:
        type: 'sql'
        dbms: 'MYSQL'
        uri: 'jdbc:mysql://<server>:3306/<database>'
        username: '<dbuser>'
        password: '<dbpass>'
`

For SQLite storage: (plugins/Waypoints/config.yml)
`storage:
    engine:
        type: 'sql'
        dbms: 'SQLITE'
        uri: 'jdbc:sqlite:path/to/waypoints.db'
`

#### Permissions Nodes

*	'waypoints.create' - Create and modify waypoints
*	'waypoints.list' - List the waypoints on the server
*	'waypoints.teleport' - Teleport to a waypoint
*	'waypoints.admin.delete' - Delete other users' waypoints
*	'waypoints.admin.edit' - Move or change the description of other users' waypoints


#### Bugs / TODO

Sorted in descending order of priority:

*	TODO: Support for more permissions systems (currently Permissions 2.7.x is REQUIRED)
*	TODO: Support for no permissions system (configurable: OPs can do this, regular users can/can't do that)
*	BUG: When using commands, the max chat length applies. This affects the maximum length of descriptions and names. (This might be fixed in a later version, something to the effect of adding description and deleting descriptions, etc.)
*	TODO: Flat file storage
*	TODO: Create POIs with signs (need a way to check sign text on deletion of sign then delete waypoint from storage)
*	TODO: Multiple/custom icons for POIs (in game limitations increase complexity)

#### Changelog - Plugin
Version 0.9.1
*	Fixed a glorious SQL connection bug where the plugin becomes useless after an hour or so running.
Version 0.9
*	Initial release

#### Changelog - Overviewer Addon
Version 1.1
*	Added Info Windows
Version 1.0
*	Initial Release
	




