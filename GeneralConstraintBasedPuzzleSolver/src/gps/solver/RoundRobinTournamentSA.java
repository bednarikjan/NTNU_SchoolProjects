package gps.solver;

import gps.Range;
import gps.statemanager.roundrobintournament.*;;

public class RoundRobinTournamentSA extends SimulatedAnnealing {

	public RoundRobinTournamentSA(int puzzleLevelInt, Range range) {
		super(new RoundRobinTournamentStateManager(puzzleLevelInt, range));
	}
}
