/*
 * Waypoints - Web Frontend - JavaScript
 * Matt Schneeberger (helluvamatt@gmail.com)
 *
 * JavaScripts for placing waypoints on the Minecraft Overviewer map.
 *
 */

var waypoints = {
	'interval' : null,
	'waypoint_markers' : [],
	'waypoint_info_windows': [],
	'perform_query' : function() {
		$.ajax( {
			type : "POST",
			url : "waypoints/waypoints.php",
			success : function(xmldata) {
				var data = $(xmldata);
				var waypoint_data = data.find('waypoint');
				
				// UPDATING WAYPOINT LOCATION MARKERS
				// 1. For Each Marker - Check if it is still needed
				for (var i in waypoints.waypoint_markers) {
					var w = waypoint_data.filter(function(j) {
						return $(this).find('name').first().text() == i;
					});
					if (w.length != 1) {
						// 1a. If not delete
						if (waypoints.waypoint_markers[i] != undefined) {
							waypoints.waypoint_markers[i].setMap(null);
							waypoints.waypoint_markers[i] = undefined;
						}
						if (waypoints.waypoint_info_windows[i] != undefined) {
							waypoints.waypoint_info_windows[i] = undefined;
						}
					} else {
						// 1b. Otherwise, update location
						var loc = w.find('location').first();
						var new_loc = overviewer.util.fromWorldToLatLng(parseFloat(loc.attr('x')), parseFloat(loc.attr('y')), parseFloat(loc.attr('z')));
						var old_loc = waypoints.waypoint_markers[i].getPosition();
						if (new_loc.lat() != old_loc.lat() || new_loc.lng() != old_loc.lng()) {
							waypoints.waypoint_markers[i].setPosition(new_loc);
							waypoints.waypoint_info_windows[i].setPosition(new_loc);
						}
					}
				}
				
				// 2. Add new markers for new waypoints
				waypoint_data.each(function(i, e) {
					var wp = $(e);
					if (waypoints.waypoint_markers[wp.find('name').first().text()] == undefined) {
						
						var loc = wp.find('location').first();
						var point = overviewer.util.fromWorldToLatLng(parseFloat(loc.attr('x')), parseFloat(loc.attr('y')), parseFloat(loc.attr('z')));
						
						var contentBody = "<div class=\"waypointInfoWindowContainer\"><div class=\"waypointInfoWindowText\"><h3>" + wp.find('name').first().text() + "</h3><p>" + wp.find('description').first().text() + "</p><p>Created by: <b>" + wp.find('owner').first().text() + "</b></p></div></div>";
						waypoints.waypoint_info_windows[i] = new google.maps.InfoWindow({
							content: contentBody
						});
						waypoints.waypoint_info_windows[i].setPosition(point);
						
						// TODO Dynamic images (IE, you get a choice of icon to use)
						var image = new google.maps.MarkerImage("waypoints/sight.png",
							new google.maps.Size(32.0, 37.0),
							new google.maps.Point(0, 0),
							new google.maps.Point(16.0, 37.0)
						);
						var shadow = new google.maps.MarkerImage("waypoints/sight-shadow.png",
							new google.maps.Size(51.0, 37.0),
							new google.maps.Point(0, 0),
							new google.maps.Point(16.0, 37.0)
						);
						 
						waypoints.waypoint_markers[i] = new google.maps.Marker();
						waypoints.waypoint_markers[i].setTitle(wp.find('name').first().text());
						waypoints.waypoint_markers[i].setIcon(image);
						waypoints.waypoint_markers[i].setShadow(shadow);
						waypoints.waypoint_markers[i].setPosition(point);
						waypoints.waypoint_markers[i].setMap(overviewer.map);
						google.maps.event.addListener(waypoints.waypoint_markers[i], 'click', function() {
							waypoints.waypoint_info_windows[i].open(overviewer.map);
						});
						
					}
				});
			}
		});
	},
	'init' : function() {
		waypoints.interval = window.setInterval(waypoints.perform_query, 10000);
		waypoints.perform_query();
	}
}