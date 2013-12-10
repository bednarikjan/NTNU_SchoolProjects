package gps;

/**
 * Class for storing the pair of integers denoting the allowed value range.
 */
public class Range extends Pair {

	/* Methods */

	// constructor
	public Range(int begin, int end) {
            super(begin, end);            
	}

	// getter
	public int begin() {
		return a;
	}

	public int end() {
		return b;
	}
}
