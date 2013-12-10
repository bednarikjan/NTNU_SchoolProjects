package gps.solver;

import gps.statemanager.LocalStateManager;

import java.util.ArrayList;

public abstract class ConstraintBasedLocalSearch {

    /* PROPERTIES */
    protected LocalStateManager stateManager;   // state manager      
    protected ArrayList<Integer> state;          // state    
    protected int step;           // number of steps

    /* METHODS */
    public abstract ArrayList<Integer> solve(int maxIterations);

    /**
     *
     * @param stateManager
     */
    public ConstraintBasedLocalSearch(LocalStateManager stateManager) {
        this.stateManager = stateManager;
        reset();
    }

    /**
     * Reset function, need to be called before each running solve
     */
    public void reset() {
        step = 0;
    }

    public int getStep() {
        return step;
    }

    public double evaluateState(ArrayList<Integer> state) {
        return stateManager.evaluateState(state);
    }

    public void printState(ArrayList<Integer> state2) {
        stateManager.printState(state2);
    }
}
