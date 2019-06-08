package se.jerka.ops.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Thing {
	
	private StringProperty name;
	private StringProperty description;
	private ObjectProperty<Position> position;
	
	public Thing(String name, String description, Position position) {
		this.name = new SimpleStringProperty(name);
		this.description = new SimpleStringProperty(description);
		this.position = new SimpleObjectProperty<Position>(position);
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

	public void setDescription(String description) {
		this.description.set(description);
		
	}

	public StringProperty descriptionProperty() {
		return description;
	}
	
	public Position getPosition() {
		return position.get();
	}
	
	public void setPosition(Position position) {
		this.position.set(position);
	}
	
	public ObjectProperty<Position> positionProperty() {
		return position;
	}

	public String toString() {
		return "[" + getName() + ", " + getDescription() + "]";
	}
	
}
