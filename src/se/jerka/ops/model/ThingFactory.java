package se.jerka.ops.model;

public class ThingFactory {
	
	public static Thing createThing(String name, String description, Position position) {
		return new Thing(name, description, position);
	}

}
