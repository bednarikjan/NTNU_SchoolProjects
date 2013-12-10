/**
 * IT3105 - Particle Swarm Optimization
 * @author Jan Bednarik
 * @author Tomas Dohnalek
 */

package pso;

import java.util.ArrayList;
import java.util.Random;

/**
 * General problem for PSO.
 * Just implement behavior below.
 */
public abstract class Problem {

    protected double particlePostionMin;
    protected double particlePostionMax;
    protected double clampMin;
    protected double clampMax;
    private static Random randomGenerator = new Random();

    /**
     * constructor
     * @param particlePostionMin
     * @param particlePostionMax 
     */
    public Problem(double particlePostionMin,
                           double particlePostionMax,
                           double clampMin,
                           double clampMax) {

        this.particlePostionMin = particlePostionMin;
        this.particlePostionMax = particlePostionMax;
        this.clampMin = clampMin;
        this.clampMax = clampMax;
    }

    public double getParticlePositionMin() {
        return particlePostionMin;
    }

    public double getParticlePositionMax() {
        return particlePostionMax;
    }

    public abstract double get(final ArrayList<Double> vector);

    /**
     * Subclasses must be able to create the objects of themselves.
     *
     * @return new instance
     */
    public abstract Problem createFitnessFunction();
    
    /**
     * Initializes particle's position
     * @param dimension
     * @return 
     */
    public abstract ArrayList<Double> initParticlePosition(int dimension);
    
    /**
     * Clamps particles position
     * @param position
     * @return 
     */
    public abstract ArrayList<Double> clampPosition(ArrayList<Double> position,
                                                    ArrayList<Double> velocity);
    
    /**
     * Clamps particles velocity
     * @param velocity
     * @return 
     */
    public abstract ArrayList<Double> clampVelocity(ArrayList<Double> velocity);
    
    /** 
     * Returns random number ranging from 'low' to 'high' including
     * @param low
     * @param high
     * @return 
     */
    public static double getRandomNumber(double low, double high) {
        return (high - low) * randomGenerator.nextDouble() + low;
    }

    /**
     * Returns list of random numbers
     * @param size
     * @param low
     * @param high
     * @return 
     */
    public static ArrayList<Double> getRandomList(int size, double low, double high) {
        ArrayList<Double> retval = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            retval.add(getRandomNumber(low, high));
        }
        return retval;
    }
}
