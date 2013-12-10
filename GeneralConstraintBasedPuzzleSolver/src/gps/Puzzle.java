package gps;
/**
 * Supported puzzles
 */
public enum Puzzle {
	KQueens("KQ"), 
	GraphColoring("GC"),
	RoundRobinTournament("RRT");

	private String puzzle;

	private Puzzle(String puzzle) {
		this.puzzle = puzzle;
	}

	public String getKey() {
		return puzzle;
	}

}
