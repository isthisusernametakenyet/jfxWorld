package se.jerka.ops.ai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.jerka.ops.model.Creature;
import se.jerka.ops.model.Creature.Individual;
import se.jerka.ops.model.Location;

public class ArtificialIntelligence { // not AI yet!
	
	private Map<Individual, Creature> creatureMap;
	private ObservableList<Creature> currentLocationCreatures;

	public ArtificialIntelligence(List<Creature> creatures, Location location) {
		List<Creature> tmp = creatures;
		currentLocationCreatures = FXCollections.observableArrayList();
		creatureMap = new HashMap<Individual, Creature>();
		int creatureIndex = 0;
		for (Individual individual : Individual.values()) {
			creatureMap.put(individual, tmp.get(creatureIndex++));
		}
		creatures(location);
	}
	
	public ObservableList<Creature> creatures(Location userLocation) {
		currentLocationCreatures.clear();
		for (Individual individual : Individual.values()) {
			String locationName = creatureMap.get(individual).currentLocation().getName();
			if (locationName.equalsIgnoreCase(userLocation.getName())) {
				currentLocationCreatures.add(creatureMap.get(individual));
			}
		}
		return currentLocationCreatures;
	}
 
}
