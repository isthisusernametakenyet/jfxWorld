package se.jerka.ops.model;

import java.util.List;

public class World {
	
	public static final int LOCATION_COL_MAX = 5;
	public static final int LOCATION_ROW_MAX = 5;
	
	private List<Location> locations;
	
	public World(List<Location> everything) {
		locations = everything;
	}
	
	public List<Location> locations() {
		return locations;
	}
	
}
