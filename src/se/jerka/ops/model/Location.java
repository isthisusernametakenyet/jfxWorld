package se.jerka.ops.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Location {
	
	public static final int POSITION_COL_MAX = 8;
	public static final int POSITION_ROW_MAX = 10; 
	
	public enum Direction {
		NORTH("north"),
		WEST("west"),
		EAST("east"),
		SOUTH("south");
		
		private String name;		
		private static final Map<String, Direction> ENUM_MAP;
		
		
		Direction (String name) {
			this.name = name;
		}
		
		static {
			Map<String, Direction> tmp = new HashMap<String, Direction>();
			for (Direction instance : Direction.values()) {
				tmp.put(instance.getName(), instance);
			}
			ENUM_MAP = Collections.unmodifiableMap(tmp);
		}
		
		public String getName() {
			return name;
		}
		
		public static Direction get(String name) {
			return ENUM_MAP.get(name);
		}
	}
	
	private StringProperty name;
	private StringProperty description;
	private ObservableList<Thing> things;
	private Location north;
	private Location west;
	private Location east;
	private Location south;
	private String path;
	
	public Location(String name,
					String description,
					Location north,
					Location west,
					Location east,
					Location south,
					List<Thing> things,
					String path) {
		this.name = new SimpleStringProperty(name);
		this.description = new SimpleStringProperty(description);
		this.north = north;
		this.west = west;
		this.east = east;
		this.south = south;
		this.things = FXCollections.observableArrayList(things);
		this.path = path;
	}

	public boolean removeItem(Thing thing) {
		return things.remove(thing);
	}
	
	public boolean putItem(Thing thing) {
		return things.add(thing);
	}
	
	public void setConnectingLocation(Location location, Direction direction) {
		switch (direction) {
		case NORTH:
			this.north = location;
			break;
		case WEST:
			this.west = location;
			break;
		case EAST:
			this.east = location;
			break;
		case SOUTH:
			this.south = location;
			break;		
		default:
			throw new IllegalArgumentException();
		}
	}
	
	public Location nextLocation(Direction direction) {
		switch (direction) {
		case NORTH:
			return north;
		case WEST:
			return west;
		case EAST:
			return east;
		case SOUTH:
			return south;
		default:
			throw new IllegalArgumentException();
		}
	}

	public boolean hasName(String name) {
		return this.getName().equalsIgnoreCase(name);
	}
	
	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public StringProperty nameProperty() {
		return name;
	}
	
	public String getDescription() {
		return description.get();
	}

	public void setDescription(String name) {
		this.description.set(name);
	}

	public StringProperty descriptionProperty() {
		return description;
	}
	
	public ObservableList<Thing> getThings() {
		return things;
	}

	public String path() {
		return path;
	}

}
