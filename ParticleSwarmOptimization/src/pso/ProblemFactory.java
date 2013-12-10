/**
 * IT3105 - Particle Swarm Optimization
 * @author Jan Bednarik
 * @author Tomas Dohnalek
 */

package pso;

import java.util.HashMap;
import pso.Problem;

/**
 * This class defines the Factory Method which returns a product - an
 * object implementin specific fitness function. The class is created as a
 * Singleton.
 */
public class ProblemFactory {
    
    /* Only one instance of this class exists. */   
    private static final ProblemFactory instance 
            = new ProblemFactory();    
    
    /* All known objects implementing Fitness Function */
    private static HashMap<String, Problem> registeredProblem 
            = new HashMap<>();

    
    private ProblemFactory() {
    }

    /**     
     * @return The only existing instance of this class.
     */
    public static ProblemFactory getInstance() {
        return instance;
    }

    /**
     * Registers the products.
     * @param id product id
     * @param f Product - Object implementning certain Fitness Function
     */
    public static void registerProblem(String id, Problem f) {
        registeredProblem.put(id, f);
    }
    
    /**
     * Creates the product.
     * @param id product id
     * @return New product
     */
    public Problem createProblem(String id) {
        return registeredProblem.get(id).createFitnessFunction();
    }
}
