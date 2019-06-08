package se.jerka.ops.ai;

import java.util.List;

import se.jerka.ops.model.Creature;
import se.jerka.ops.model.Location;

public class AIManager {
	
	public static ArtificialIntelligence createAI(
			List<Creature> creatures,
			Location location) {
		return new ArtificialIntelligence(creatures, location);
	}
}
