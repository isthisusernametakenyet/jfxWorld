package se.jerka.ops.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Creature {
	
	public enum Individual {
		DWARF,
		TROLL,
		GIANT;
	}
	
	private StringProperty name;
	private ObjectProperty<Location> currentLocation;
	private ObjectProperty<Position> position;
	
	public Creature(String name, Location location, Position position) {
		this.name = new SimpleStringProperty(name);
		this.currentLocation = new SimpleObjectProperty<Location>(location);
		this.position = new SimpleObjectProperty<Position>(position);
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

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public StringProperty nameProperty() {
		return name;
	}
	
	public Position getPosition() {
		return position.get();
	}
	
	public void setPpsition(Position position) {
		this.position.set(position);
	}
	
	public ObjectProperty<Position> positionProperty() {
		return position;
	}

	public String toString() {
		return "[" + getName() + "]";
	}

}
