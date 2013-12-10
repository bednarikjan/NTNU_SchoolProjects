package gps.solver;

import java.util.Vector;

import gps.statemanager.graphcolor.GraphColorStateManager;
import gps.statemanager.graphcolor.GraphColorVertex;
import gps.Range;

public class GraphColorSA extends SimulatedAnnealing {

    public GraphColorSA(Vector<GraphColorVertex> vertices, Range domain) throws Exception {
        super(new GraphColorStateManager(vertices, domain));
    }
}
