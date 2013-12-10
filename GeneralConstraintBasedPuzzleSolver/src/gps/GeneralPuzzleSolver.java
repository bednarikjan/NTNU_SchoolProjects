package gps;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import gps.solver.*;
import gps.statemanager.graphcolor.GraphColorParser;
import gps.statemanager.graphcolor.GraphColorVertex;
import gnu.getopt.*;

public class GeneralPuzzleSolver {
	// name of program

	final private static String progname = "GeneralPuzzleSolver";
	private static Puzzle puzzle = Puzzle.GraphColoring;
	private static Method method = Method.MinimumConflicts;
	private static int rounds = 20;
	private static String puzzleLevelS;
	private static int puzzleLevelInt;
	private static int MAX_STEPS = 100000;
	private static int GRAPH_COLORS = 4;
	private static Boolean visualize = false;

	public static void main(String[] args) {
		Boolean showHelp = false;

		// parameter parsing
		try {
			showHelp = !parseCmdParams(args);
		} catch (Exception e) {
			showHelp = true;
			System.err.println("ERROR: " + e.getMessage());
		} finally {
			if (showHelp) {
				usage(progname);
				return;
			}
		}

		ConstraintBasedLocalSearch solver;

		// choosing the solver according to parameters (MC or SA for given
		// puzzle)
		try {
			solver = chooseSolver();
		} catch (Exception e) {
			System.err.println("ERROR: " + e.getMessage());
			return;
		}

		solve(solver, rounds);
	}

	/**
	 * Chooses the appropriate solver (SA or MC for gven puzzle)
	 * 
	 * @return
	 * @throws Exception
	 */
	public static ConstraintBasedLocalSearch chooseSolver() throws Exception {
		ConstraintBasedLocalSearch solver = null;

		if (puzzle == Puzzle.GraphColoring) {
			Vector<GraphColorVertex> vertices = new Vector<GraphColorVertex>();
			if (!GraphColorParser.parse(puzzleLevelS, vertices)) {
				throw new Exception(
						"Graph color parser cannot parse input file.");
			}
			switch (method) {
			case MinimumConflicts:
				solver = new GraphColorMC(vertices, new Range(0,
						GRAPH_COLORS - 1));
				break;
			case SimulatedAnnealing:
				solver = new GraphColorSA(vertices, new Range(0,
						GRAPH_COLORS - 1));
				break;

			default:
				System.err.println("ERROR: This should never happen!");
				System.exit(-1);
				break;
			}
		} else if (puzzle == Puzzle.KQueens) {
			switch (method) {
			case MinimumConflicts:
				solver = new KQueensMC(puzzleLevelInt, new Range(0,
						puzzleLevelInt - 1));
				break;

			case SimulatedAnnealing:
				solver = new KQueensSA(puzzleLevelInt, new Range(0,
						puzzleLevelInt - 1));
				break;

			default:
				System.err.println("ERROR: This should never happen!");
				System.exit(-1);
				break;
			}
		} else { // ROUND ROBIN TOURNAMENT
			switch (method) {
			case MinimumConflicts:
				solver = new RoundRobinTournamentMC(puzzleLevelInt, new Range(
						1, puzzleLevelInt));
				break;

			case SimulatedAnnealing:
				solver = new RoundRobinTournamentSA(puzzleLevelInt, new Range(
						1, puzzleLevelInt));
				break;

			default:
				System.err.println("ERROR: This should never happen!");
				System.exit(-1);
				break;
			}
		}

		return solver;
	}

	/**
	 * Calls the solver to solve the puzzle and prints the statistics
	 * 
	 * @param solver
	 * @param rounds
	 */
	public static void solve(ConstraintBasedLocalSearch solver, int rounds) {
		ArrayList<Result> results = new ArrayList<>();
		for (int i = 0; i < rounds; i++) {
			solver.reset();
			ArrayList<Integer> state = solver.solve(MAX_STEPS);
			results.add(new Result(solver.getStep(), solver
					.evaluateState(state)));
			if (visualize) {
				solver.printState(state);
			}
			System.out.println("Found solution with quality "
					+ solver.evaluateState(state) + " in step "
					+ solver.getStep());
		}

		printStatistics(results);

	}

	/**
	 * Print stastistics for given repeated results.
	 * @param results
	 */
	private static void printStatisticsForRounds(List<Result> results) {
		ArrayList<Integer> steps = new ArrayList<>();
		ArrayList<Double> qualities = new ArrayList<>();
		for (Result rs : results) {
			steps.add(rs.getSteps());
			qualities.add(rs.getQuality());
		}
		DecimalFormat df = new DecimalFormat("#.##");

		double meanOfQualities = getMean(qualities);
		double meanOfSteps = getMean(steps);
		double bestEvaluation = Collections.min(qualities);
		double stddevOfQualities = getStandardDeviation(qualities);
		double stddevOfSteps = getStandardDeviation(steps);
		int quickestSolution = Collections.max(results).getSteps();
		System.out.println("Best evaluation: " + bestEvaluation);

		System.out.println("Quickest solution: "
				+ ((bestEvaluation == 0.0) ? quickestSolution
						: "no optimal solution found"));
		System.out.println("Avarage evaluation of result: "
				+ df.format(meanOfQualities) + " +-"
				+ df.format(stddevOfQualities));
		System.out.println("Avarage number of steps: " + df.format(meanOfSteps)
				+ " +-" + df.format(stddevOfSteps));
		System.out.println("--------------------------------------");
	}

	/**
	 * Print detailed statistics for given repeated results.
	 * @param results
	 */
	private static void printStatistics(ArrayList<Result> results) {
		System.out.println("--------------------------------------");
		System.out.println("All " + rounds + " runs");
		printStatisticsForRounds(results);
		int bestOfRuns = 20;
		if (results.size() > bestOfRuns) { // do best-of-run statiscits
			Collections.sort(results);
			Collections.reverse(results);
			System.out.println(bestOfRuns + " best-of-runs");
			printStatisticsForRounds(results.subList(0, bestOfRuns));
		}
	}

	private static Boolean parseCmdParams(String[] argv) throws Exception {
		Boolean retval = true;
		int c;
		String arg;
		String level = null;

		Getopt g = new Getopt(progname, argv, "hvp:l:r:m:");
		g.setOpterr(false); // We'll do our own error handling
		//

		while ((c = g.getopt()) != -1) {
			switch (c) {
			case 'p': // puzzle selection
				arg = g.getOptarg();
				if (arg == null) {
					retval = false;
				} else if (arg.compareTo(Puzzle.KQueens.getKey()) == 0) {
					puzzle = Puzzle.KQueens;
				} else if (arg.compareTo(Puzzle.GraphColoring.getKey()) == 0) {
					puzzle = Puzzle.GraphColoring;
				} else if (arg.compareTo(Puzzle.RoundRobinTournament.getKey()) == 0) {
					puzzle = Puzzle.RoundRobinTournament;
				} else {
					throw new Exception("unrecognized puzzle");
				}
				break;
			//
			case 'l': // level selection
				arg = g.getOptarg();
				if (arg == null) {
					retval = false;
				}
				level = new String(arg);
				break;
			case 'r': // rounds count
				arg = g.getOptarg();
				if (arg == null) {
					retval = false;
				}
				rounds = Integer.parseInt(arg);
				break;
			//

			case 'v': // visualize flag
				visualize = true;
				break;

			case 'h': // help
				retval = false;
				break;
			//
			case 'm': // method
				arg = g.getOptarg();
				if (arg == null) {
					retval = false;
				} else if (arg.compareTo(Method.MinimumConflicts.getKey()) == 0) {
					method = Method.MinimumConflicts;
				} else if (arg.compareTo(Method.SimulatedAnnealing.getKey()) == 0) {
					method = Method.SimulatedAnnealing;
				} else {
					retval = false;
				}
				break;
			case '?':
				System.out.println("The option '" + (char) g.getOptopt()
						+ "' is not valid");
				break;
			//
			default:
				System.out.println("getopt() returned " + c);
				break;
			}
		}
		//
		for (int i = g.getOptind(); i < argv.length; i++) {
			System.out.println("Non option argv element: " + argv[i] + "\n");
		}

		// parse puzzle level;
		if (level == null) {
			return false;
		} else if (puzzle == Puzzle.GraphColoring) {
			puzzleLevelS = level;
		} else if (puzzle == Puzzle.KQueens) {
			puzzleLevelInt = Integer.parseInt(level);
		} else if (puzzle == Puzzle.RoundRobinTournament) {
			puzzleLevelInt = Integer.parseInt(level);
		} else {
			throw new Exception("unrecognized puzzle");
		}

		return retval;

	}

	/**
	 * Prints usage of this program ti stdout.
	 * 
	 * @param progname
	 *            name of this program
	 */
	private static void usage(String progname) {
		System.out.println("USAGE: " + progname + " [-v] [-p PUZZLE] "
				+ "-l LEVEL [-r ROUNDS] [-m METHOD]");
		System.out.println("Solve puzzle using given method.");
		System.out.println();
		System.out.println("  -v        \tvisualize result");
		System.out.println("  -p PUZZLE \tpuzzle selection");
		System.out.println("            \tPUZZLE = [KQ|GC|MS] where");
		System.out.println("            \t\tKQ is KQueens,");
		System.out
				.println("           \t\tGC is Graph Coloring, this is implicit,");
		System.out.println("           \t\tRRT is Round Robin Tournament");
		System.out.println("  -l LEVEL  \tlevel selection");
		System.out.println("            \tLEVEL= [INT|FILENAME] where");
		System.out
				.println("           \t\tINT is number of queens in case of KQ");
		System.out
				.println("           \t\tFILENAME is path to source file in case of GC");
		System.out
				.println("           \t\tINT is size of square in case of MS");
		System.out
				.println("  -r ROUNDS\texpress count of how many times puzzle is being solved.");
		System.out.println("         \timplicit is 20");
		System.out.println("  -m METHOD selection of method ");
		System.out.println("         \tMETHOD = [SA|MC] where");
		System.out.println("         \t\tSA is Simulated Annealing");
		System.out
				.println("         \t\tMC is Minimum Conflicts, this is implicit value");

	}

	/**
	 * Returns a mean (or average) of given list of numbers.
	 * 
	 * @param list
	 *            list of numbers
	 * @return mean
	 */
	private static <T extends Number> double getMean(ArrayList<T> list) {
		double mean = 0;
		for (T i : list) {
			mean += i.doubleValue();
		}
		return mean / list.size();
	}

	/**
	 * Function counts standard deviation.
	 * 
	 * @param list
	 *            list of number
	 * @return standard deviation
	 */
	private static <T extends Number> double getStandardDeviation(
			ArrayList<T> list) {
		double mean = getMean(list);
		double stddev = 0;
		for (T i : list) {
			stddev += Math.pow(i.doubleValue() - mean, 2);
		}
		return Math.sqrt(stddev / list.size());
	}

}
