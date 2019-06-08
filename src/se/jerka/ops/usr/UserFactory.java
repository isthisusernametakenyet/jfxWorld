package se.jerka.ops.usr;

import java.util.List;

import se.jerka.ops.model.Location;
import se.jerka.ops.model.Position;
import se.jerka.ops.model.Thing;

public class UserFactory {
	
	public static User createUser(
			List<Thing> inventory,
			Location location,
			Position position) {
		return new User(inventory, location, position);
	}

}
