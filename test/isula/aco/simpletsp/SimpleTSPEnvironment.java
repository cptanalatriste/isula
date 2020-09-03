package isula.aco.simpletsp;

import isula.aco.Environment;

public class SimpleTSPEnvironment extends Environment {

    public static final int[][] SAMPLE_PROBLEM = {{0, 10, 12, 11, 14},
            {10, 0, 13, 15, 8},
            {12, 13, 0, 9, 14},
            {11, 15, 9, 0, 16},
            {14, 8, 14, 16, 0}};


    private int[][] problemRepresentation;

    @Override
    protected double[][] createPheromoneMatrix() {
        if (problemRepresentation != null) {
            return new double[problemRepresentation.length][problemRepresentation.length];
        }

        return null;
    }

    public int[][] getProblemRepresentation() {
        return problemRepresentation;
    }

    public void setProblemRepresentation(int[][] problemRepresentation) {
        this.problemRepresentation = problemRepresentation;
        this.setPheromoneMatrix(this.createPheromoneMatrix());
    }


}
