package gps.solver;

import gps.Range;
import gps.statemanager.roundrobintournament.RoundRobinTournamentStateManager;

public class RoundRobinTournamentMC extends MinConflicts {
	public RoundRobinTournamentMC(int puzzleLevelInt, Range range) {
		super(new RoundRobinTournamentStateManager(puzzleLevelInt, range));
	}
}
