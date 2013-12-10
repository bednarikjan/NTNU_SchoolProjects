package gps.statemanager.graphcolor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class GraphColorParser {

	/**
	 * Parse graph color file and save vertices.
	 * @param filename file to be parsed
	 * @param vertices output vector
	 * @return true if process was successful
	 */
	public static Boolean parse(String filename,
			Vector<GraphColorVertex> vertices) {

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			// header parsing
			ArrayList<Integer> list;
			String line = br.readLine();
			list = parseStringToIntList(line);
			if (list.size() != 2)
				throw new IOException("Wrong format of file `" + filename + "`");

			int numberOfVerticies = list.get(0);
			vertices.setSize(numberOfVerticies);
			int numberOfEdges = list.get(1);

			// vertices position
			ArrayList<Double> doubleList;
			for (int i = 0; i < numberOfVerticies; i++) {
				line = br.readLine();
				doubleList = parseStringToDoubleList(line);
				if (doubleList.size() != 3 || doubleList.get(0) != i) {
					throw new IOException("Wrong format of file `" + filename
							+ "`");
				}
				vertices.set(i, new GraphColorVertex(i, doubleList.get(1),
						doubleList.get(2)));
			}

			// edges connection
			for (int i = 0; i < numberOfEdges; i++) {
				line = br.readLine();
				list = parseStringToIntList(line);
				if (list.size() != 2) {
					throw new IOException("Wrong format of file `" + filename
							+ "`");
				}
				// set connections
				vertices.get(list.get(0)).addNeighbour(list.get(1));
				vertices.get(list.get(1)).addNeighbour(list.get(0));
			}
			
			// excess lines
			if ((line = br.readLine()) != null) {
				System.err.println("WARNING: Excess lines in file" + filename);
			}
		} catch (IOException e) {
			System.err.format("ERROR: %s%n", e);
			return false;
		}

		return true;
	}

	private static ArrayList<Integer> parseStringToIntList(String line) {
		ArrayList<Integer> result = new ArrayList<Integer>();

		if (line != null) {
			try {
				String[] tokens = line.split(" ");
				for (String token : tokens) {
					result.add(Integer.parseInt(token));
				}
			} catch (NumberFormatException e) {
				System.err.format("Error: %s%n", e);
			}
		}
		return result;
	}

	private static ArrayList<Double> parseStringToDoubleList(String line) {
		ArrayList<Double> result = new ArrayList<Double>();

		if (line != null) {
			try {
				String[] tokens = line.split(" ");
				for (String token : tokens) {
					result.add(Double.parseDouble(token));
				}
			} catch (NumberFormatException e) {
				System.err.format("Error: %s%n", e);
			}
		}
		return result;
	}

}
