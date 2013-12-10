package gps.statemanager.roundrobintournament;

import java.util.ArrayList;
import java.util.Collections;

import gps.Range;
import gps.Pair;
import gps.statemanager.LocalStateManager;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RoundRobinTournamentStateManager extends LocalStateManager {

    private int rounds;         
    private int matchesInRound;

    /**
     * Constructor
     * @param size number of teams
     * @param domain lowest and highest team number
     */
    public RoundRobinTournamentStateManager(int size, Range domain) {
        super(size, domain);

        if ((size & 1) != 0) { // odd
            size++;
            this.domain = new Range(domain.begin(), domain.end() + 1);
        }

        rounds = size - 1;
        matchesInRound = size / 2;
        this.size = matchesInRound * rounds; // since common size is not correct
    }

    /**
     * Initialize
     * @return state
     */
    public ArrayList<Integer> getInitialState() {
        ArrayList<Integer> state = new ArrayList<>(size * 2);
        for (int r = 0; r < rounds; r++) { // for each round
            ArrayList<Integer> leftovers = new ArrayList<>();
            for (int i = domain.begin(); i <= domain.end(); i++) {
                leftovers.add(i);
            }

            while (leftovers.size() > 0) {
                int number = leftovers.remove(getRandomInt(0,
                        leftovers.size() - 1));

                state.add(number);
            }
        }

        return state;
    }

    /**
     * Generate new state randomly (for SA)
     * @param state
     * @return new state
     */
    @Override
    public ArrayList<Integer> getRandomNeighbour(ArrayList<Integer> state) {
        ArrayList<Integer> neighbour = new ArrayList<Integer>(state);
        int round = getRandomInt(0, rounds - 1);
        int match1 = getRandomInt(0, matchesInRound - 1);
        int match2 = getRandomInt(0, matchesInRound - 1);
        Collections.swap(neighbour, round * matchesInRound * 2 + match1 * 2, round * matchesInRound * 2 + match2 * 2);
        return neighbour;
    }

    /**
     * Generate the best possible new state (for MC)
     * @param state
     * @param conflictMatch chosen variable to change - team pair
     * @return new state
     */
    @Override
    public ArrayList<Integer> getInteligentNeighbour(ArrayList<Integer> state,
            int conflictMatch) {

        ArrayList<Integer> possibleSwaps = new ArrayList<>();

        // row boundaries
        int row = conflictMatch / (matchesInRound * 2);
        int min = row * (matchesInRound * 2);
        int max = min + (matchesInRound * 2) - 1;

        double best = evaluateState(state);
        possibleSwaps.add(conflictMatch);

        // find new better position
        for (int i = min; i < max; i++) {
            // current state
            if ((i == conflictMatch) || (i == conflictMatch + 1)) {
                continue;
            }

            ArrayList<Integer> newState = new ArrayList<Integer>(state);           
            swapTeams(newState, i, conflictMatch);                        

            // find best new position
            double quality = evaluateState(newState);
            if (quality < best) {
                best = quality;
                possibleSwaps.clear();
                possibleSwaps.add(i);
            } else if (quality == best) {
                possibleSwaps.add(i);
            }
        }       

        // randomly choose from the same evaluated new positions
        int randIdx = LocalStateManager.getRandomInt(0, possibleSwaps.size()-1);
        swapTeams(state, conflictMatch, possibleSwaps.get(randIdx));        
        
        return state;
    }

    /**
     * Swap two teams in one round
     * @param state
     * @param idxA
     * @param idxB 
     */
    private void swapTeams(ArrayList<Integer> state, int idxA, int idxB) {
        int aux = state.get(idxA);
        state.set(idxA, state.get(idxB));
        state.set(idxB, aux);
    }
   
    @Override
    public double evaluateState(ArrayList<Integer> state) {
        double quality = 0.0;   // the less the better
        Map<Pair, Boolean> conflicts = new HashMap<>();

        for (int i = 0; i < 2 * size; i += 2) {
            Pair match = new Pair(state.get(i), state.get(i + 1));

            // sort team number in pair: [lower,higher]
            if (match.geta() > match.getb()) {
                match.swap();
            }

            if (conflicts.containsKey(match)) {
                if (!conflicts.get(match)) { // first conflict for current match
                    conflicts.put(match, true);
                    quality += 0.5;
                }
                quality += 0.5;
            } else {
                conflicts.put(match, false);
            }
        }

        return quality;
    }

    /**
     * Visualize given state
     * @param state 
     */
    @Override
    public void printState(ArrayList<Integer> state) {
    	return;/*
        for (int r = 0; r < rounds; r++) {
            for (int i = 0; i < matchesInRound; i += 1) {
                System.out.print(state.get(r * matchesInRound * 2 + i * 2));
                System.out.print("-");
                System.out.print(state.get(r * matchesInRound * 2 + i * 2 + 1));
                System.out.print(" ");
            }
            System.out.println();
        }*/
    }

    /**
     * Get conflicting pairs of teams (for MC)
     * @param state
     * @return list of conflicting pairs
     */
    @Override
    public ArrayList<Integer> getConflicts(ArrayList<Integer> state) {
        // number of conflicts for each variable
        Map<Pair, Integer> conflictsMap = new HashMap<>();
        HashSet<Integer> conflictsIndices = new HashSet<Integer>();

        // iterate through all variables (pairs team-team)
        for (int i = 0; i < 2 * size; i += 2) {
            Pair match = new Pair(state.get(i), state.get(i + 1));

            // sort team number in pair: [lower,higher]
            if (match.geta() > match.getb()) {
                match.swap();
            }

            // save indices of conflicting variables
            if (conflictsMap.containsKey(match)) {
                conflictsIndices.add(i);
                conflictsIndices.add(conflictsMap.get(match));
            } else {
                conflictsMap.put(match, i);
            }
        }

        return new ArrayList<Integer>(conflictsIndices); // list of unique values
    }
}
