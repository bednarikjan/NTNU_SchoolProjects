package gps;

/**
 * Results of the runs for statistics.
 */
public class Result implements Comparable<Result>{
	int steps;
	double quality;
	
	public Result(int steps, double quality) {
		this.steps = steps;
		this.quality = quality;
	}
	
	public int getSteps() {
		return steps;
	}
	
	public double getQuality() {
		return quality;
	}

	@Override
	public int compareTo(Result arg0) {
		if (quality == arg0.getQuality()) {
			return arg0.getSteps() - steps;
		} else {
			return (int) (arg0.getQuality() - quality);
		}
	}
}