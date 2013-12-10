package gps.statemanager.kqueens;

import gps.statemanager.LocalStateManager;
import java.util.ArrayList;
import gps.Range;
import java.util.Collections;
import java.util.HashSet;

public class KQueensStateManager extends LocalStateManager {

    /* METHODS */
    public KQueensStateManager(int size, Range domain) {
        super(size, domain);
    }
    
    /**
     * Check whether 2 queens are conflicting (for MC)
     * @param qi queen 1
     * @param qj queen 2
     * @param state
     * @return 
     */
    private boolean isConflict(int qi, int qj, ArrayList<Integer> state) {
        return (state.get(qi) ==  state.get(qj))                      || 
               (state.get(qi) == (state.get(qj) + Math.abs(qi - qj))) ||
               (state.get(qi) == (state.get(qj) - Math.abs(qi - qj)));
    }
    
    /**
     * Returns list of conflicting queens (for MC)
     * @param state
     * @return 
     */
    @Override
    public ArrayList<Integer> getConflicts(ArrayList<Integer> state) {
        HashSet<Integer> conflicts = new HashSet<Integer>();
        
        for(int qi = 0; qi < size; qi++) {            
            for(int qj = qi + 1; qj < size; qj++) {                
                // check conflicts - 2 queens in the same column or diagonal
                if(isConflict(qi, qj, state)) {                    
                    conflicts.add(qi);
                    conflicts.add(qj);
                }
            }
        }
        
        return new ArrayList<Integer>(conflicts);
    }
    
    /**
     * Generate the best possible new state (for MC)
     * @param state
     * @param conflictQueen
     * @return 
     */
    @Override
    public ArrayList<Integer> getInteligentNeighbour(ArrayList<Integer> state, 
                                                     int conflictQueen) {
        
        ArrayList<Integer> newState  = new ArrayList<Integer>(state);
        ArrayList<Integer> conflicts = new ArrayList<>();
        for(int i = 0; i < size; i++) conflicts.add(0);
        
        // get conflicts for all possible queen's positions
        for(int i = 0; i < size; i++) {
            newState.set(conflictQueen, i);
            for(int otherQueen = 0; otherQueen < size; otherQueen++) {                
                if(conflictQueen == otherQueen) continue;
                if(isConflict(conflictQueen, otherQueen, newState))
                    conflicts.set(i, conflicts.get(i) + 1);
            }
        }
        
        // place queen to the best position
        int currentConflicts = conflicts.get(state.get(conflictQueen));        
        int minConflicts     = Collections.min(conflicts);
        
        if(currentConflicts == minConflicts) { /* no better position */
            // find columns of the same number of conflicts
            ArrayList<Integer> possibleColumns = new ArrayList<>();
            for(int c = 0; c < size; c++) {
                if(conflicts.get(c) == minConflicts) {
                    possibleColumns.add(c);           
                }
            }
            
            
            // randomly choose one of the other best positions (if any)
            possibleColumns.remove(new Integer(state.get(conflictQueen)));

            if(!possibleColumns.isEmpty()) {
                int column = getRandomInt(0, possibleColumns.size() - 1);
                state.set(conflictQueen, possibleColumns.get(column));
            }            
        } else {    /* better position found */
            int column = conflicts.indexOf(minConflicts);
            state.set(conflictQueen, column);
        }
        
        return state;
    }
    
    /**
     * Visualize given state
     * @param state 
     */
    @Override
    public void printState(ArrayList<Integer> state) {
        // 1st line:
        // + - - - - - - - - - +
        System.out.format("+ ");
        for (int i = 0; i < size; i++) {
            System.out.format("- ");
        }
        System.out.format("+%n");

        // 2nd - "size+1"th line
        for (int i = 0; i < size; i++) {
            System.out.format("| ");
            for (int j = 0; j < size; j++) {
                System.out.format((state.get(i) == j) ? "X " : ". ");
            }
            System.out.format("|%n");
        }

        // last line
        // + - - - - - - - - - +
        System.out.format("+ ");
        for (int i = 0; i < size; i++) {
            System.out.format("- ");
        }
        System.out.format("+%n");
    }

    /**
     * Generate new state randomly (for SA)
     * @param state
     * @return 
     */
    @Override
    public ArrayList<Integer> getRandomNeighbour(ArrayList<Integer> state) {
    	ArrayList<Integer> retval = new ArrayList<>(state);
    	
    	int row = getRandomInt(domain.begin(), domain.end());
    	int column = getRandomInt(domain.begin(), domain.end());
    	
    	retval.set(row, column);
    	return retval;
    }

	@Override
	public double evaluateState(ArrayList<Integer> state) {
		double quality = 0.0;
		for(int qi = 0; qi < size; qi++) {            
            for(int qj = 0; qj < size; qj++) {                
                // check conflicts - 2 queens in the same column or diagonal
                if(qi != qj && isConflict(qi, qj, state)) {                    
                    quality += 0.5;
                }
            }
        }
		return quality;
	}
}
