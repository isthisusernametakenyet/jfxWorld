package se.jerka.ops.init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.jerka.ops.ai.ArtificialIntelligence;
import se.jerka.ops.ai.AIManager;
import se.jerka.ops.io.DataReader;
import se.jerka.ops.model.Creature;
import se.jerka.ops.model.CreatureFactory;
import se.jerka.ops.model.CsvCreature;
import se.jerka.ops.model.DummyLocation;
import se.jerka.ops.model.Location;
import se.jerka.ops.model.WorldFactory;
import se.jerka.ops.model.Thing;
import se.jerka.ops.model.World;
import se.jerka.ops.model.Location.Direction;
import se.jerka.ops.model.Position;
import se.jerka.ops.usr.User;
import se.jerka.ops.usr.UserFactory;

public class Initializer {
	
	private static Initializer instance;
	
	private DataReader reader;
	private List<Location> locations;
	private List<Creature> creatures;
	
	public static Initializer getInstance() {
		if (instance == null) {
			instance = new Initializer();
		}
		return instance;
	}
	
	private Initializer() {
		reader = new DataReader();
	}
	
	public World initWorld() {
		return WorldFactory.createWorld(initLocations());
	}
	
	public User initUser() {
		return UserFactory.createUser(initInventory(), initUserLocation(), initUserPosition());
	}
	
	public ArtificialIntelligence initAI(Location location) {
		return AIManager.createAI(initCreatures(), location);
	}

	private List<Location> initLocations() {
		locations = new ArrayList<Location>();
		List<DummyLocation> dummies = reader.readLocations(DummyLocation.WORLD_PATH);	
		for (DummyLocation dummy : dummies) {
			List<Thing> things = reader.readThings(dummy.getPath());
			locations.add(WorldFactory.createLocation(
					dummy.getName(),
					dummy.getDescription(),
					null,
					null,
					null,
					null,
					things,
					dummy.getPath()
			));
		}
		for (int i = 0; i < locations.size(); i++) {
			Map<Direction, Integer> idMap = new HashMap<>();
			int directionIndex = 0;
			for (Direction direction : Direction.values()) {
				idMap.put(direction, dummies.get(i).potentialExits().get(directionIndex++));
			}
			for (Direction direction : Direction.values()) {
				if (DummyLocation.isExit(idMap.get(direction))) {
					locations.get(i).setConnectingLocation(locations.get(idMap.get(direction)), direction);
				}
			}
		}
		return Collections.unmodifiableList(locations);
	}

	private List<Creature> initCreatures() {
		creatures = new ArrayList<Creature>();
		List<CsvCreature> csvCreatures = reader.readCreatures(CsvCreature.CREATURE_PATH);
		for (Location location : locations) {
			for (CsvCreature csvCreature : csvCreatures) {
				String name = csvCreature.getLocationName();
				if (location.getName().equalsIgnoreCase(name)) {
					creatures.add(CreatureFactory.createCreature(
							csvCreature.getName(),
							location,
							WorldFactory.createPosition(csvCreature.x(), csvCreature.y())
					));
				}
			}
		}
		return Collections.unmodifiableList(creatures);
	}

	private Location initUserLocation() {
		return reader.readUserLocation(Collections.unmodifiableList(locations), User.LOCATION_PATH);
	}
	
	private List<Thing> initInventory() {
		return Collections.unmodifiableList(reader.readThings(User.INVENTORY_PATH));
	}
	
	private Position initUserPosition() {
		return WorldFactory.createPosition(User.START_X, User.START_Y);
	}
	
	

}
