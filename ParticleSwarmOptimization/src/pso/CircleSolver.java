/**
 * IT3105 - Particle Swarm Optimization
 * @author Jan Bednarik
 * @author Tomas Dohnalek
 */

package pso;

/**
 * Specific solver for circle problem 
 *
 */
public class CircleSolver extends Solver {

	public CircleSolver(Problem problem, int maxIterations, int dimension,
			double epsilon, double inertiaWeightStart, double inertiaWeightEnd,
			int connections, double c1, double c2) {
		super(problem, maxIterations, dimension, epsilon, inertiaWeightStart,
				inertiaWeightEnd, connections, c1, c2);
		int particleCount = 10 + (int) (10 * Math.sqrt(dimension));
        for (int i = 0; i < particleCount; i++) {
            particles.add(new Particle(dimension));
        }
        
        // find the best global position and set it to all of them
        updateBestGlobalPosition(connections);
	}

}
