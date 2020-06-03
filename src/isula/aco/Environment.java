package isula.aco;

import isula.aco.exception.ConfigurationException;

import java.util.Arrays;

/**
 * The place that our ants traverse, where they gather the information to build
 * solutions.
 * <p>
 * <p>
 * This class manages the access to the problem representation - as an array
 * of doubles- and to the pheromone matrix. Each concrete class needs to define a
 * way to build a pheromone matrix to the problem to be solved.
 *
 * @author Carlos G. Gavidia
 */
public abstract class Environment {


    // TODO(cgavidia): We're supporting pheromone deposition on vertex. On other
    // problems, the pheromone is deposited on edges.
    private double[][] pheromoneMatrix;

    /**
     * Creates a pheromone matrix depending of the nature of the problem to solve.
     * When overriding this method, you can call getProblemRepresentation() to obtain
     * problem context information.
     *
     * @return Pheromone matrix instance.
     */
    protected abstract double[][] createPheromoneMatrix();

    /**
     * Creates an Environment for the Ants to traverse.
     */
    public Environment() {
        this.pheromoneMatrix = createPheromoneMatrix();

    }

    public void setPheromoneMatrix(double[][] pheromoneMatrix) {
        this.pheromoneMatrix = pheromoneMatrix;
    }

    public double[][] getPheromoneMatrix() {
        return pheromoneMatrix;
    }


    /**
     * Assigns the same value to all cells on the Pheromone Matrix.
     *
     * @param pheromoneValue Value to assign.
     */
    public void populatePheromoneMatrix(double pheromoneValue) {
        if (pheromoneMatrix == null || pheromoneMatrix.length == 0) {
            throw new ConfigurationException("The pheromone matrix is not properly configured. Verify the implementation of " +
                    "the createPheromoneMatrix() method.");
        }

        int matrixRows = pheromoneMatrix.length;
        int matrixColumns = pheromoneMatrix[0].length;

        for (int i = 0; i < matrixRows; i++) {
            for (int j = 0; j < matrixColumns; j++) {
                pheromoneMatrix[i][j] = pheromoneValue;
            }
        }
    }

    /**
     * Multiplies every cell in the pheromone matrix by a factor.
     *
     * @param factor Factor for multiplication.
     */
    public void applyFactorToPheromoneMatrix(double factor) {
        int matrixRows = pheromoneMatrix.length;
        int matrixColumns = pheromoneMatrix[0].length;

        for (int i = 0; i < matrixRows; i++) {
            for (int j = 0; j < matrixColumns; j++) {
                double newValue = pheromoneMatrix[i][j] * factor;
                pheromoneMatrix[i][j] = newValue;
            }
        }
    }

}
