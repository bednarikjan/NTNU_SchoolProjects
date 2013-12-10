/**
 * IT3105 - Particle Swarm Optimization
 * @author Jan Bednarik
 * @author Tomas Dohnalek
 */

package pso;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class Solver {
	protected Problem problem;
	protected int maxIterations;
	protected int dimension;
	protected double epsilon;
	protected int rounds;
	protected double inertiaWeightEnd;
	protected double inertiaWeightStart;
	protected int connections;
	protected ArrayList<Particle> particles = new ArrayList<>();
	protected double c2;
	protected double c1;
	protected int step;
	protected double inertiaStep;

	public Solver(Problem problem, int maxIterations, int dimension,
			double epsilon, double inertiaWeightStart, double inertiaWeightEnd,
			int connections, double c1, double c2) {
		this.problem = problem;
		this.maxIterations = maxIterations;
		this.dimension = dimension;
		this.epsilon = epsilon;
		this.inertiaWeightStart = inertiaWeightStart;
		this.inertiaWeightEnd = inertiaWeightEnd;
		this.inertiaStep = (inertiaWeightStart - inertiaWeightEnd)
				/ maxIterations;
		this.connections = connections;
		this.c1 = c1;
		this.c2 = c2;

		Particle.setC1(c1);
		Particle.setC2(c2);
		Particle.setW(inertiaWeightStart);
		Particle.setFitness(problem);
		this.particles = new ArrayList<>();

	}

	public int solve() throws FileNotFoundException,
			UnsupportedEncodingException {
		for (step = 0; step < maxIterations; step++) {
			doStep();

			double fitnessValue = problem.get(getBestGlobalPosition());
			if (fitnessValue < epsilon) {
				System.out.println("#Found solution in step " + (step + 1)
						+ " with fitness function " + fitnessValue);

				return step + 1;
			}
		}

		System.out.println("#Solution not found");
		return maxIterations;
	}

	private void doStep() {
		System.out.println((step + 1) + " "
				+ problem.get(getBestGlobalPosition()));
		updateParticles();
	}

	// DEBUG
	private void printParticles() {
		System.out.println("===================");
		printParticlesData();
		System.out.println("===================");
	}

	private void updateParticles() {
		updateParticlesVelocity();
		updateParticlesPosition();
		updateParticlesInertia();
		updateBestGlobalPosition(connections);
	}

	private void updateParticlesInertia() {
		Particle.setW(Particle.getW() - inertiaStep);
	}

	protected void updateBestGlobalPosition(int connections) {
		if (connections == -1) {
			setBestGlobalPositionToAllParticles(getBestGlobalPosition());
		} else {
			for (Particle p : particles) {
				// pick closest neighbours
				ArrayList<Particle> possibleNeighbours = new ArrayList<>(
						particles);
				Collections.sort(possibleNeighbours,
						new ParticleDistanceComparator(p));
				// pick connections + 1 closest particles (+1 because the
				// examined particle is also in the list
				possibleNeighbours = new ArrayList<>(
						possibleNeighbours.subList(0, connections + 1));
				p.setBestGlobalPosition(getBestGlobalPosition(possibleNeighbours));
			}
		}
	}

	class ParticleDistanceComparator implements Comparator<Particle> {

		Particle particle;

		public ParticleDistanceComparator(Particle p) {
			particle = p;
		}

		private double getDistance(Particle p) {
			double distance = 0;
			for (int i = 0; i < particle.getPosition().size(); i++) {
				distance += Math.pow(particle.getPosition().get(i)
						- p.getPosition().get(i), 2);
			}
			return Math.sqrt(distance);
		}

		@Override
		public int compare(Particle o1, Particle o2) {
			double o1distance = getDistance(o1);
			double o2distance = getDistance(o2);
			return Double.compare(o1distance, o2distance);
		}
	}

	private void printParticlesData() {
		for (int i = 0; i < particles.size(); i++) {
			if (i == 1) {
				particles.get(i).print(i);
			}
		}
	}

	private void updateParticlesPosition() {
		for (Particle particle : particles) {
			particle.updatePosition();
		}
	}

	private void updateParticlesVelocity() {
		for (Particle particle : particles) {
			particle.updateVelocity();
		}
	}

	private void setBestGlobalPositionToAllParticles(ArrayList<Double> pos) {
		for (Particle p : particles) {
			p.setBestGlobalPosition(pos);
		}
	}

	private ArrayList<Double> getBestGlobalPosition(
			ArrayList<Particle> particles) {
		ArrayList<Double> retval = new ArrayList<>();
		double best = Double.MAX_VALUE;
		for (Particle p : particles) {
			ArrayList<Double> position = p.getBestParticlePosition();
			double fitnessValue = problem.get(position);
			// DEBUG
			// System.out.println("fitness: " + fitnessValue);
			// System.out.println("position: " + position);

			if (fitnessValue < best) {
				retval = new ArrayList<Double>(position);
				best = fitnessValue;
			}
		}
		return retval;
	}

	protected ArrayList<Double> getBestGlobalPosition() {
		return getBestGlobalPosition(particles);
	}
}
