package se.jerka.ops.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

import se.jerka.ops.model.CreatureFactory;
import se.jerka.ops.model.CsvCreature;
import se.jerka.ops.model.DummyLocation;
import se.jerka.ops.model.Location;
import se.jerka.ops.model.WorldFactory;
import se.jerka.ops.model.Thing;
import se.jerka.ops.model.ThingFactory;

public class DataReader {
	
	public List<Thing> readThings(String path) {
		List<Thing> features = new ArrayList<Thing>();
		try (
				FileInputStream fis = new FileInputStream(path);
				InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
				CSVReader reader = new CSVReader(isr)) {
			String[] row;
			while ((row = reader.readNext()) != null) {
				features.add(
						ThingFactory.createThing(
								row[0],
								row[1],
								WorldFactory.createPosition(
										Integer.parseInt(row[2]),
										Integer.parseInt(row[3])
								)
						)
				);
 			} 
		} catch (IOException ioe) {
			System.err.println("Unable to read " + path + " : " + ioe.getMessage());
			System.exit(1);
		}
		return features;
	}
	
	public List<DummyLocation> readLocations(String path) {
		List<DummyLocation> dummies = new ArrayList<DummyLocation>();
		try (
				FileInputStream fis = new FileInputStream(path);
				InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
				CSVReader reader = new CSVReader(isr)) {
			String[] row;
			while ((row = reader.readNext()) != null) {
				dummies.add(
						WorldFactory.createDummyLocation(
								row[0],
								row[1],
			 					Integer.parseInt(row[2]), 
			 					Integer.parseInt(row[3]),	
			 					Integer.parseInt(row[4]),
			 					Integer.parseInt(row[5]),
			 					row[6]
			 			)
				);
			} 
		} catch (IOException ioe) {
			System.err.println("Unable to read " + path + " : " + ioe.getMessage());
			System.exit(1);
		}
		return dummies;
	}
	
	public List<CsvCreature> readCreatures(String path) {
		List<CsvCreature> creatures = new ArrayList<CsvCreature>();
		try (
				FileInputStream fis = new FileInputStream(path);
				InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
				CSVReader reader = new CSVReader(isr)) {
			String[] row;
			while ((row = reader.readNext()) != null) {
				creatures.add(CreatureFactory.createCsvCreature(row[0], row[1], row[2], row[3]));
			}
		} catch (IOException ioe) {
			System.err.println("Unable to read " + path + " : " + ioe.getMessage());
			System.exit(1);
		}
		return creatures;
	}
	
	public Location readUserLocation(List<Location> locations, String path) {
		String locationName = null;
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        locationName = line;
		    }
		} catch (IOException ioe) {
			return null;
		}
		Location savedLocation = null;
		for (Location loc : locations) {
			if (locationName.equals(loc.getName())) {
				savedLocation = loc;
			}
		}
		return savedLocation;
	}

}
