package se.jerka.ops.model;

import java.util.List;

import javafx.collections.FXCollections;

public class WorldFactory {
	
	public static World createWorld(List<Location> locations) {
		return new World(locations);
	}
	
	public static Location createLocation(String name,
										  String description,
										  Location north,
										  Location west,
										  Location east,
										  Location south,
										  List<Thing> items,
										  String path) {
		return new Location(name, 
							description, 
							north, 
							west,
							east, 
							south,  
							FXCollections.observableArrayList(items), 
							path);
	}
	
	public static DummyLocation createDummyLocation(String name, 
									  String description, 
									  int north,
									  int west,
									  int east, 
									  int south, 
									  String path) {
		return new DummyLocation(name, description, north, west, east, south, path);
	}
	
	public static Position createPosition(int x, int y) {
		return new Position(x, y);
	}
	
}