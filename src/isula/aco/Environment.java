package isula.aco;

import isula.aco.exception.InvalidInputException;

public abstract class Environment {

  private double[][] problemGraph;

  // TODO(cgavidia): We're supporting pheromone deposition on vertex. On other
  // problems, the pheromone is deposited on edges.
  private double[][] pheromoneMatrix;

  /**
   * Creates an Environment for the Ants to traverse.
   * 
   * @param problemGraph
   *          Graph representation of the problem to be solved.
   * @throws InvalidInputException
   *           When the problem graph is incorrectly formed.
   */
  public Environment(double[][] problemGraph) throws InvalidInputException {
    this.problemGraph = problemGraph;
    this.pheromoneMatrix = createPheromoneMatrix();

    if (!this.isProblemGraphValid()) {
      throw new InvalidInputException();
    }
  }

  protected boolean isProblemGraphValid() {
    return true;
  }

  public double[][] getProblemGraph() {
    return problemGraph;
  }

  public double[][] getPheromoneMatrix() {
    return pheromoneMatrix;
  }

  protected abstract double[][] createPheromoneMatrix();

  /**
   * Asigns the same value to all cells on the Pheromone Matrix.
   * 
   * @param pheromoneValue
   *          Value to assign.
   */
  public void populatePheromoneMatrix(double pheromoneValue) {
    int matrixRows = pheromoneMatrix.length;
    int matrixColumns = pheromoneMatrix.length;

    for (int i = 0; i < matrixRows; i++) {
      for (int j = 0; j < matrixColumns; j++) {
        pheromoneMatrix[i][j] = pheromoneValue;
      }
    }
  }

  /**
   * Multiplies every cell in the pheromone matrix by a factor.
   * 
   * @param factor
   *          Factor for multiplication.
   */
  public void applyFactorToPheromoneMatrix(double factor) {
    int matrixRows = pheromoneMatrix.length;
    int matrixColumns = pheromoneMatrix.length;

    for (int i = 0; i < matrixRows; i++) {
      for (int j = 0; j < matrixColumns; j++) {
        double newValue = pheromoneMatrix[i][j] * factor;
        pheromoneMatrix[i][j] = newValue;
      }
    }
  }

}
