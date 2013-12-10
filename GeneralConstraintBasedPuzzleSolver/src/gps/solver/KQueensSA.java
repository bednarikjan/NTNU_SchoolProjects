package gps.solver;

import gps.statemanager.kqueens.KQueensStateManager;
import gps.Range;

public class KQueensSA extends SimulatedAnnealing {
    
        /**
         * Constructor
         * @param size
         * @param domain 
         */
	public KQueensSA(int size, Range domain) {
		super(new KQueensStateManager(size, domain));
	}
}
