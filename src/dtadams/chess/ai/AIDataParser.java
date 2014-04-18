package dtadams.chess.ai;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AIDataParser {

	HashMap<String, Double> fields;

	public AIDataParser(String path) {
		fields = new HashMap<>();
		parse(path);
	}

	void parse(String path) {

		try(FileInputStream fis = new FileInputStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

			while(true) {
				String line = reader.readLine();
				if(line == null) break;

				String[] parts = line.split(":");

				if(parts.length == 2) {
					String field = parts[0];
					double value = Double.parseDouble(parts[1]);

					fields.put(field, value);
				}
				else throw new Exception("Invalid line in file "+path+": "+line);
			}
		} catch(Exception e) {
			System.out.println(e);
			System.out.println(e.getStackTrace());
		}
	}

	public double get(String field) {
		return fields.get(field);
	}

	// Writes data to a file
	public static void write(HashMap<String, Double> fields, String path) {
		// TODO
	}
}