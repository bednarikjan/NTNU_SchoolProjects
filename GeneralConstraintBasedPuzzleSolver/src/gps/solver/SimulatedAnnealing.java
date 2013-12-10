package gps.solver;

import java.util.ArrayList;
import java.util.Random;

import gps.statemanager.LocalStateManager;

public class SimulatedAnnealing extends ConstraintBasedLocalSearch {

	protected double T_MAX = 10;		// starting temperature
	protected double T_ALPHA = 0.9999; // cooling coeficient
	protected double T_EPSILON = 0.01; // min temperature

	public SimulatedAnnealing(LocalStateManager stateManager) {
		super(stateManager);
	}

	/**
	 * Classic Simulated Annealing simple approach from Artificial
	 *  Inteligence: A Modern Approach
	 */
	public ArrayList<Integer> solve(int maxIterations) {
		state = stateManager.getInitialState();
		double quality = evaluateState(state);
		
		double T = T_MAX;
		for (step = 0; step < maxIterations && quality > 0.0; step++) {
			if (T < T_EPSILON) // T is too small
				break;
			ArrayList<Integer> neighbour = stateManager.getRandomNeighbour(state);
			double qualityNeighbour = evaluateState(neighbour);
			double delta = quality - qualityNeighbour;
			if (delta > 0) { // exploiting
				state = neighbour;
				quality = qualityNeighbour;
			} else {
				Random r = new Random();
				double x = r.nextDouble();
				if (x < Math.pow(Math.E, delta/T)) { // exploring
					state = neighbour;
					quality = qualityNeighbour;
				}
			}
			T *= T_ALPHA;
		}
		return state;
	}
}
