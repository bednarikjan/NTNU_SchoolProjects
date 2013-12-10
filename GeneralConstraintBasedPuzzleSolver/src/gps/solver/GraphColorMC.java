package gps.solver;

import java.util.Vector;

import gps.Range;
import gps.statemanager.graphcolor.GraphColorStateManager;
import gps.statemanager.graphcolor.GraphColorVertex;

public class GraphColorMC extends MinConflicts {
    
    /* METHODS */
    public GraphColorMC(Vector<GraphColorVertex> vertices, Range domain) throws Exception {
        super(new GraphColorStateManager(vertices, domain));
    }
}