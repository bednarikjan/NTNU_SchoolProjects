package gps.statemanager.graphcolor;

import gps.statemanager.*;

import java.util.ArrayList;
import java.util.Vector;

import gps.Range;

public class GraphColorStateManager extends LocalStateManager {
	// vertices
	Vector<GraphColorVertex> vertices;

	/**
	 * Initialization
	 * 
	 * @param vertices
	 * @param domain
	 * @throws Exception
	 */
	public GraphColorStateManager(Vector<GraphColorVertex> vertices,
			Range domain) throws Exception {
		super(vertices.size(), domain);
		this.vertices = vertices;
	}

	/**
	 * Returns random neighbour to given state.
	 */
	public ArrayList<Integer> getRandomNeighbour(ArrayList<Integer> state) {
		ArrayList<Integer> neighbour = new ArrayList<Integer>(state);
		int index = getRandomInt(0, state.size() - 1);
		neighbour.set(index, getRandomInt(domain.begin(), domain.end()));
		return neighbour;
	}

	/**
	 * Returns a "better" state close to given state. "Better" is expressed
	 * according to evaluation function
	 */
	@Override
	public ArrayList<Integer> getInteligentNeighbour(ArrayList<Integer> state,
			int conflictVertex) {
		ArrayList<Integer> retval = new ArrayList<>(state);

		double best = evaluateState(state);

		ArrayList<Integer> possibleColor = new ArrayList<>();
		possibleColor.add(state.get(conflictVertex));

		for (int color = domain.begin(); color <= domain.end(); color++) {
			if (color != state.get(conflictVertex)) {
				ArrayList<Integer> modified = new ArrayList<>(state);
				modified.set(conflictVertex, color);
				double quality = evaluateState(modified);
				if (quality < best) {
					// retval = modified;
					best = quality;
					possibleColor.clear();
					possibleColor.add(color);
				} else if (quality == best) {
					possibleColor.add(color);
				}
			}
		}

		retval.set(conflictVertex,
				possibleColor.get(getRandomInt(0, possibleColor.size() - 1)));

		return retval;
	}

	/**
	 * Evaluates given state. Returns 0.0 if state is perfect solution, more
	 * otherwise.
	 */
	public double evaluateState(ArrayList<Integer> state) {
		double sum = 0;
		for (int i = 0; i < size; ++i) {
			GraphColorVertex v = vertices.get(i);
			int color = state.get(i);
			for (int neighbourIndex : v.getNeighbours()) {
				if (color == state.get(neighbourIndex)) {
					sum += 0.5;
				}
			}
		}

		return sum;
	}

	/**
	 * Prints given state in source code of graphviz.
	 */
	public void printState(ArrayList<Integer> state) {
		final String[] colors = { "#0066B3", "#FFE500", "#00CC00", "#CC0000" };

		System.out.println("graph {");
		System.out.println("rankdir = LR;");
		for (int i = 0; i < state.size(); i++) {
			System.out.println("\t\"" + i + "\""
					+ " [style = \"filled\",fillcolor = \""
					+ colors[state.get(i)] + "\"]");
		}
		for (int i = 0; i < state.size(); i++) {
			GraphColorVertex v = vertices.get(i);
			for (int neigh : v.getNeighbours()) {
				if (neigh > i) {
					System.out.println("\t\"" + i + "\" -- \"" + neigh + "\"");
				}
			}
		}
		System.out.println("}");
	}

	@Override
	/**
	 * Returns indexes of vertices that are in conflict.
	 */
	public ArrayList<Integer> getConflicts(ArrayList<Integer> state) {
		assert state.size() == vertices.size();

		ArrayList<Integer> conflicts = new ArrayList<>();

		for (int i = 0; i < state.size(); ++i) {
			int vColor = state.get(i);
			GraphColorVertex v = vertices.get(i);
			for (int neighbourIndex : v.getNeighbours()) {
				if (vColor == state.get(neighbourIndex)) {
					conflicts.add(i);
					break;
				}
			}
		}

		return conflicts;
	}
}
