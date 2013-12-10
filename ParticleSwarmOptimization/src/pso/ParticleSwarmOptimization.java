/**
 * IT3105 - Particle Swarm Optimization
 * @author Jan Bednarik
 * @author Tomas Dohnalek
 */

package pso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import pso.knapsack.KnapsackSolver;

/**
 * Main class of PSO
 */
public class ParticleSwarmOptimization {

    static {
        try {
            /* load all necessary classes */
            Class.forName("pso.CircleProblem");
            Class.forName("pso.KnapsackProblem");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ParticleSwarmOptimization.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // To use '.' as a decimal separator.
        Locale.setDefault(Locale.ENGLISH);                  
    }   
    
    /**
     * @param args the command line arguments
     * @throws UnsupportedEncodingException 
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        if (args.length != 1) { //
            usage("ParticleSwarmOptimization");
            return;
        }

        Configuration config;
        try {
            config = new Configuration(args[0]);
        } catch (NoSuchFieldException | IOException e) {
            e.printStackTrace();
            return;
        }

        /* Create the factory */
        ProblemFactory ffFactory = ProblemFactory.getInstance();

        /* Factory creates the product - fitness function */
        Problem ff = ffFactory.createProblem(config.problem);

        if (ff instanceof KnapsackProblem) {
        	new KnapsackSolver((KnapsackProblem)ff, config.maxIterations, config.dimension, config.epsilon,
                    config.inertiaWeightStart,
                    config.inertiaWeightEnd, config.connections, config.c1,
                    config.c2, config.weightLimit, config.volumeLimit, 
                    config.knapsackInputFile).solve();	
        } else {
        	new CircleSolver(ff, config.maxIterations, config.dimension, config.epsilon,
                    config.inertiaWeightStart,
                    config.inertiaWeightEnd, config.connections, config.c1,
                    config.c2).solve();
        }
        
    }

    public static void usage(String progname) {
        System.out.println("Usage: " + progname + " CONFIG_FILE");
        System.out.println("Solves problem using particle swarm optimalization.");
    }
}
