package se.jerka.ops.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import com.opencsv.CSVWriter;

import se.jerka.ops.model.Thing;

public class DataWriter {
	
	private CSVWriter dataWriter;

	public boolean save(Thing t, String destination, String source) throws IOException {
		dataWriter = new CSVWriter(new FileWriter(destination, true));
		String[] row = { 
				t.getName(), 
				t.getDescription(),
				Integer.toString(t.getPosition().x()),
				Integer.toString(t.getPosition().y()) 
		};
		dataWriter.writeNext(row);
		dataWriter.flush();
		dataWriter.close();
		return deleteOld(t, source);
	}

	public boolean deleteOld(Thing item, String path) throws IOException {
		List<Thing> temp = new DataReader().readThings(path);
		for (Iterator<Thing> iterator = temp.listIterator(); iterator.hasNext(); ) {
			Thing t = iterator.next();
		    if (t.getName().equals(item.getName())) {
		        iterator.remove();
		    }
		}
		dataWriter = new CSVWriter(new FileWriter(path));
		for (Thing t : temp) {
			String[] row = { 
					t.getName(), 
					t.getDescription(), 
					Integer.toString(t.getPosition().x()),
					Integer.toString(t.getPosition().y()) 
			};
			dataWriter.writeNext(row);
		}
		dataWriter.flush();
		dataWriter.close();
		return true;
	}

	public boolean writeUserLocation(String name, String path) {
		Writer output = null;
		try {
			output = new BufferedWriter(new FileWriter(path));
			output.write(name);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return true; // but why
	}

}
