package se.jerka.ops.model;

public class CsvCreature {
	
	public static final String CREATURE_PATH = "data/creatures.csv";
	
	private String name;
	private String locationName;
	private int x;
	private int y;
	
	public CsvCreature(
			String name,
			String locationName,
			int x,
			int y) {
		this.setName(name);
		this.setLocationName(locationName);
		this.x = x;
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}

}
