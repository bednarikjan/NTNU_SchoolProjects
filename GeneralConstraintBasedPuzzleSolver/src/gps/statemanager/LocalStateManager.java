package gps.statemanager;

import gps.Range;

import java.util.ArrayList;
import java.util.Random;

public abstract class LocalStateManager {

	/* ATRIBUTES */

	protected int size; // number of variables
	protected Range domain = new Range(0, 0); // variables' domain

	/* METHODS */

        /**
         * constructor
         * @param size number of variables
         * @param domain range of correct values for variables
         */
	public LocalStateManager(int size, Range domain) {
		this.size = size;
		this.domain = domain;
	}

        /**
         * Generate new state randomly (for SA)
         * @param state
         * @return new state
         */
	public abstract ArrayList<Integer> getRandomNeighbour(
			ArrayList<Integer> state);

        /**
         * Generate the best possible new state (for MC)
         * @param state
         * @param variable chosen conflicting variable to be changed
         * @return new state
         */
	public abstract ArrayList<Integer> getInteligentNeighbour(
			ArrayList<Integer> state, int variable);

        /**
         * Evaluates the given state. The lower the value the better.
         * (0.0 is complete solution)
         * @param state
         * @return evaluation
         */
	public abstract double evaluateState(ArrayList<Integer> state);

        /**
         * Visualiza given state
         * @param state 
         */
	public abstract void printState(ArrayList<Integer> state);

	/**
	 * Randomly initializes a state of the puzzle.
	 * 
	 * @return
	 */
	public ArrayList<Integer> getInitialState() {
		ArrayList<Integer> state = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			state.add(getRandomInt(domain.begin(), domain.end()));
		}

		return state;
	}

        /**
         * Returns random integer within given range
         * @param min
         * @param max
         * @return 
         */
	public static int getRandomInt(int min, int max) {
		Random r = new Random();
		return min + (int) r.nextInt(max - min + 1);
	}

        /**
         * Returns list of conflicting variables (for MC)
         * @param state
         * @return 
         */
	public abstract ArrayList<Integer> getConflicts(ArrayList<Integer> state);
}
