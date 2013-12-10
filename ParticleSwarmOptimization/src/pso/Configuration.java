/**
 * IT3105 - Particle Swarm Optimization
 * @author Jan Bednarik
 * @author Tomas Dohnalek
 */

package pso;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Messenger class for loading configuration files.
 *
 */
public class Configuration {

    public String problem;
    public int dimension;
    public int maxIterations;
    public double epsilon;
    public int connections;
    public double inertiaWeightStart;
    public double inertiaWeightEnd;
    public double c1;
    public double c2;
    public double weightLimit = 0.0;
    public double volumeLimit = 0.0;
    public String knapsackInputFile = "";
    private Properties defaultProps;

    public Configuration(String path) throws IOException, NoSuchFieldException {
        defaultProps = new Properties();
        FileInputStream in = new FileInputStream(path);
        defaultProps.load(in);
        in.close();

        setValues(defaultProps);
    }

    /**
     * Sets all values from configuration file
     * @param defaultProps
     * @throws NoSuchFieldException
     */
    private void setValues(Properties defaultProps) throws NoSuchFieldException {
        problem = parseString("problem");
        dimension = parseInt("dimension");
        maxIterations = parseInt("max_iterations");
        epsilon = parseDouble("epsilon");
        connections = parseInt("connections");
        inertiaWeightStart = parseDouble("inertia_weight_start");
        inertiaWeightEnd = parseDouble("inertia_weight_end");
        c1 = parseDouble("c1");
        c2 = parseDouble("c2");

        // specific additional configuration for knapsack problem
        if (problem.equals("knapsack")) {
            weightLimit = parseDouble("weight_limit");
            volumeLimit = parseDouble("volume_limit");
            knapsackInputFile = parseString("knapsack_input_file");
        }
    }

    /**
     * Parse string or yields exception.
     * @param key
     * @return
     * @throws NoSuchFieldException
     */
    private String parseString(String key) throws NoSuchFieldException {
        String value = defaultProps.getProperty(key);
        if (value == null) {
            throw new NoSuchFieldException(key);
        }
        return value;
    }

    /**
     * Parse integer or yields exception.
     * @param key
     * @return
     * @throws NoSuchFieldException
     */
    private int parseInt(String key) throws NoSuchFieldException {
        String value = defaultProps.getProperty(key);
        if (value == null) {
            throw new NoSuchFieldException(key);
        }
        return Integer.parseInt(value);
    }

    /**
     * Parse double or yields exception.
     * @param key
     * @return
     * @throws NoSuchFieldException
     */
    private double parseDouble(String key) throws NoSuchFieldException {
        String value = defaultProps.getProperty(key);
        if (value == null) {
            throw new NoSuchFieldException(key);
        }
        return Double.parseDouble(value);
    }
}
