/**
 * IT3105 - Particle Swarm Optimization
 * @author Jan Bednarik
 * @author Tomas Dohnalek
 */

package pso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class implements behavior of a single particle of swarm. 
 *
 */
public class Particle {

	private ArrayList<Double> position;
	private ArrayList<Double> velocity;
	private ArrayList<Double> bestParticlePosition = new ArrayList<>();
	private ArrayList<Double> bestGlobalPosition = new ArrayList<>();
	private static double c1;
	private static double c2;
	private static double w;
	private static Problem fitness;
	private int dimension;

	/**
	 * Be sure to set bestGlobalPosition after this call before updating
	 * velocity/position.
	 * 
	 * @param dimension
	 * @param c1
	 * @param c2
	 * @param w
	 */
	public Particle(int dimension) {
		this.dimension = dimension;
		this.init(dimension);
	}

	private void init(int dimension) {
		// velocity
		velocity = new ArrayList<>();
		for (int i = 0; i < dimension; i++) // starting velocity is zero
		{
			//velocity.add(Problem.getRandomNumber(-4, 4));
			velocity.add(0.0);
		}

		// position
		position = fitness.initParticlePosition(dimension);

		// set current position as best position seen so far
		bestParticlePosition = new ArrayList<Double>(position);
	}

	public void setBestGlobalPosition(final ArrayList<Double> p) {
		bestGlobalPosition = new ArrayList<Double>(p);
	}

	public final ArrayList<Double> getBestParticlePosition() {
		return bestParticlePosition;
	}

	public final ArrayList<Double> getBestGlobalPosition() {
		return bestGlobalPosition;
	}

	public static double getC1() {
		return c1;
	}

	public static double getC2() {
		return c2;
	}

	public static double getW() {
		return w;
	}

	public static void setW(double w) {
		Particle.w = w;
	}

	public ArrayList<Double> getVelocity() {
		return velocity;
	}

	/**
	 * position(t+1) = position(t) + velocity(t+1). Be sure that you called
	 * updateVelocity first!
	 */
	public void updatePosition() {
		position = sumLists(position, velocity);
		position = fitness.clampPosition(position, velocity);

		if (fitness.get(position) < fitness.get(bestParticlePosition)) {
			bestParticlePosition = position;
		}
	}

	/**
	 * velocity(t+1) = w*(velocity(t) + c1 * r1 * (bestPosition(t) -
	 * position(t)) + c2 * r2 * (bestGlobalPosition(t) - position(t)))
	 */
	public void updateVelocity() {
		double r1 = fitness.getRandomNumber(0, 1);
		double r2 = fitness.getRandomNumber(0, 1);

		ArrayList<Double> pTxT = subtractLists(bestParticlePosition, position);
		ArrayList<Double> gTxT = subtractLists(bestGlobalPosition, position);

		ArrayList<Double> c1r1pTxT = multiplyList(c1 * r1, pTxT);
		ArrayList<Double> c2r2pTxT = multiplyList(c2 * r2, gTxT);

		ArrayList<Double> wv = multiplyList(w, velocity);
		ArrayList<Double> vTc1r1pTxT = sumLists(wv, c1r1pTxT);
		ArrayList<Double> vTc1r1pTxTc2r2pTxT = sumLists(vTc1r1pTxT, c2r2pTxT);

		velocity = new ArrayList<Double>(vTc1r1pTxTc2r2pTxT);
		velocity = fitness.clampVelocity(velocity);
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return [a_1 - b_1, a_2 - b_2, ...]
	 */
	public static ArrayList<Double> subtractLists(final ArrayList<Double> a,
			final ArrayList<Double> b) {
		ArrayList<Double> retval = new ArrayList<Double>();
		if (a.size() != b.size()) {
			throw new ArrayIndexOutOfBoundsException("");
		}
		for (int i = 0; i < a.size(); i++) {
			retval.add(a.get(i) - b.get(i));
		}
		return retval;
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return [a_1 + b_1, a_2 + b2, ...]
	 */
	public static ArrayList<Double> sumLists(final ArrayList<Double> a,
			final ArrayList<Double> b) {
		ArrayList<Double> retval = new ArrayList<Double>();
		if (a.size() != b.size()) {
			throw new ArrayIndexOutOfBoundsException("");
		}
		for (int i = 0; i < a.size(); i++) {
			retval.add(a.get(i) + b.get(i));
		}
		return retval;
	}

	/**
	 * @param c
	 * @param list
	 * @return c*list_i for all i
	 */
	public static ArrayList<Double> multiplyList(final double c,
			final ArrayList<Double> list) {
		ArrayList<Double> retval = new ArrayList<Double>();
		for (int i = 0; i < list.size(); i++) {
			retval.add(c * list.get(i));
		}
		return retval;
	}

	public void print(int particleID) {
		System.out.println("Particle " + particleID);

		// position
		System.out.print("\tposition             = ");
		printArrayListDouble(position, 3);

		// velocity
		System.out.print("\tvelocity             = ");
		printArrayListDouble(velocity, 3);

		// best positions
		System.out.print("\tbestParticlePosition = ");
		printArrayListDouble(bestParticlePosition, 3);

		System.out.print("\tbestGlobalPosition   = ");
		printArrayListDouble(bestGlobalPosition, 3);

		// fitness
		System.out.println("\tfitness = " + fitness.get(position));

		// knapsack properties
		if (fitness instanceof KnapsackProblem) {
			System.out.println("\tvalue  = "
					+ ((KnapsackProblem) fitness).knapsackQuality(position,
							Package.PackageAttributes.VALUE));
			System.out.println("\tweight = "
					+ ((KnapsackProblem) fitness).knapsackQuality(position,
							Package.PackageAttributes.WEIGHT));
		}

	}

	public void printArrayListDouble(ArrayList<Double> vec, int decPlaces) {
		String format = "%." + decPlaces + "f";

		System.out.print("[");
		System.out.printf(format, vec.get(0));

		for (int i = 1; i < vec.size(); i++) {
			System.out.print(", ");
			System.out.printf(format, vec.get(i));
		}
		System.out.println("]");
	}

	public static void setC1(double arg) {
		c1 = arg;
	}

	public static void setC2(double arg) {
		c2 = arg;
	}

	public ArrayList<Double> getPosition() {
		return position;
	}

	public static void setFitness(Problem arg) {
		fitness = arg;
	}

	// DEBUG
	public void printPosition() {
		System.out.println(position);
	}
}
