package se.jerka.ops.usr;

import java.io.IOException;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.jerka.ops.io.DataWriter;
import se.jerka.ops.model.Location;
import se.jerka.ops.model.Position;
import se.jerka.ops.model.Thing;
import se.jerka.ops.model.Location.Direction;

public class User {
	
	public final static int START_X = 2;
	public final static int START_Y = 2;
	
	public final static String VISUAL_REPRESENTATION = "player";
	
	public static final String INVENTORY_PATH = "data/inventory.csv";
	public static final String LOCATION_PATH = "data/user_location.txt";
	
	private static final String WRITE_FAILURE = "unable to save :-( ";
	
	public enum Mode {
		MACRO,
		MICRO,
		INVENTORY;
	}
	
	private ObservableList<Thing> inventory;
	private ObjectProperty<Location> currentLocation;
	private ObjectProperty<Position> currentPosition;
	private DataWriter memory;
	private Mode currentMode;
	
	public User(List<Thing> inventory, Location location, Position position) {
		this.currentLocation = new SimpleObjectProperty<Location>(location);
		this.inventory = FXCollections.observableArrayList(inventory);
		memory = new DataWriter();
		currentPosition = new SimpleObjectProperty<Position>(position);
		currentMode = Mode.MACRO;
	}
	
	public void move(Direction direction) {
		switch (currentMode) {
		case MACRO:
			Location location = currentLocation().nextLocation(direction);
			currentLocation.set(location);
			memory.writeUserLocation(location.getName(), User.LOCATION_PATH);
			break;
		case MICRO:
			Position position = currentPosition().nextPosition(direction);
			currentPosition.set(position);
			break;
		case INVENTORY:
			break;
			default: throw new IllegalArgumentException();
		}
	}
	
	public void take(Thing thing) throws IOException {
		if (!currentLocation().removeItem(thing) || !inventory.add(thing)) {
			throw new IllegalArgumentException();
		}
		if (!memory.save(thing, INVENTORY_PATH, currentLocation().path())) {
			throw new IOException(WRITE_FAILURE);
		}
	}
	
	public Thing inspectPosition() {
		Thing thing = null;
		for (Thing t : currentLocation().getThings()) {
			if (t.getPosition().x() == currentPosition().x()
					&& t.getPosition().y() == currentPosition().y()) {
				thing = t;
			}
		}
		return thing;
	}
	
	public void drop(Thing thing) throws IOException {
		thing.setPosition(currentPosition());
		if (!inventory.remove(thing) || !currentLocation().putItem(thing)) {
			throw new IllegalArgumentException();
		}
		if (!memory.save(thing, currentLocation().path(), INVENTORY_PATH)) {
			throw new IOException(WRITE_FAILURE);
		}
	}
	
	public boolean hasExitIn(Direction direction) {
		return currentLocation.get().nextLocation(direction) != null;
	}

	public ObservableList<Thing> inventory() {
		return FXCollections.unmodifiableObservableList(inventory);
	}
	
	public void setInventory(ObservableList<Thing> inventory) {
		this.inventory = inventory;
		
	}

	public Location currentLocation() {
		return currentLocation.get();
	}

	public void setCurrentLocation(Location location) {
		this.currentLocation.set(location);
	}

	public ObjectProperty<Location> currentLocationProperty() {
		return currentLocation;
	}
	
	public Position currentPosition() {
		return currentPosition.get();
	}
	
	public void setCurrentPosition(Position position) {
		this.currentPosition.set(position);
	}
	
	public ObjectProperty<Position> currentPositionProperty() {
		return currentPosition;
	}
	
	public Mode currentMode() {
		return currentMode;
	}
	
	public void setCurrentMode(Mode mode) {
		this.currentMode = mode;
	}

}