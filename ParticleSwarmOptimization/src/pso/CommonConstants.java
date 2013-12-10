/**
 * IT3105 - Particle Swarm Optimization
 * @author Jan Bednarik
 * @author Tomas Dohnalek
 */

package pso;

/**
 *  Class holds program's common constants.
 */
public class CommonConstants {
    public static final double circleProblemParticlePositionMin = -100;
    public static final double circleProblemParticlePositionMax = +100;
    public static final double knapsackProblemParticlePositionMin = 0.0;    // not used
    public static final double knapsackProblemParticlePositionMax = +1.0;   // not used
    public static final double knapsackProblemClampVelocityMin = -1000;
    public static final double knapsackProblemClampVelocityMax = +1000;
    public static final double knapsackProblemPackageVolumeMin = 1;
    public static final double knapsackProblemPackageVolumeMax = 100;
}
