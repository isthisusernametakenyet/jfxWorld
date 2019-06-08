package se.jerka.ops.model;

public class CreatureFactory {
	
	public static Creature createCreature(
			String name,
			Location location,
			Position position) {
		return new Creature(name, location, position);
	}

	public static CsvCreature createCsvCreature(
			String name,
			String locationName,
			String x,
			String y) {
		return new CsvCreature(name, locationName, Integer.parseInt(x), Integer.parseInt(y));
	}
}
