package se.jerka.ops.model;

import java.util.ArrayList;
import java.util.List;

public class DummyLocation {
	
	private String name;
	private String description;
	private int north;
	private int west;
	private int east;
	private int south;
	private String path;
	List<Integer> potentialExits;
	
	public final static String WORLD_PATH = "data/locations.csv";
	
	public DummyLocation(
			String name, 
			String description,
			int north, 
			int west, 
			int east, 
			int south, 	
			String path) {
		
		this.setName(name);
		this.setDescription(description);
		this.setNorth(north);
		this.setWest(west);
		this.setEast(east);
		this.setSouth(south);
		this.setPath(path);
		potentialExits = new ArrayList<>();
		potentialExits.add(getNorth());
		potentialExits.add(getWest());
		potentialExits.add(getEast());
		potentialExits.add(getSouth());
	}
	
	public List<Integer> potentialExits() {
		return this.potentialExits;
	}
	
	public static boolean isExit(int id) {
		return id > -1;
	}

	private int getNorth() {
		return north;
	}

	private void setNorth(int north) {
		this.north = north;
	}

	private int getEast() {
		return east;
	}

	private void setEast(int east) {
		this.east = east;
	}

	private int getSouth() {
		return south;
	}

	private void setSouth(int south) {
		this.south = south;
	}

	private int getWest() {
		return west;
	}

	private void setWest(int west) {
		this.west = west;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	private void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	private void setPath(String path) {
		this.path = path;
	}

}
