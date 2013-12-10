package gps;

/**
 * Supported methods for solving the puzzles
 */
public enum Method {
	MinimumConflicts("MC"), 
	SimulatedAnnealing("SA");
	
	private String method;
	
	private Method(String method) {
		this.method = method;
	}
	
	public String getKey() {
		return method;
	}
}
