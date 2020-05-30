package isula.aco;

import isula.aco.exception.InvalidInputException;

public class MatrixEnvironment extends Environment {

    private double[][] problemRepresentation;

    public MatrixEnvironment(double[][] problemRepresentation) throws InvalidInputException {
        super();
        this.problemRepresentation = problemRepresentation;

        if (!this.isProblemRepresentationValid()) {
            throw new InvalidInputException();
        }

    }

    /**
     * Verifies if the problem graph matrix provided is valid. By default this
     * method returns true: override if necessary.
     *
     * @return True if valid, false otherwise.
     */
    protected boolean isProblemRepresentationValid() {
        return true;
    }

    public double[][] getProblemRepresentation() {
        return problemRepresentation;
    }

    @Override
    protected double[][] createPheromoneMatrix() {
        return new double[0][];
    }

    @Override
    public String toString() {
        return "Problem Representation: Rows " + problemRepresentation.length + " Columns "
                + problemRepresentation[0].length + "\n" + "Pheromone Matrix: Rows "
                + getPheromoneMatrix().length + " Columns " + getPheromoneMatrix()[0].length;

    }
}
