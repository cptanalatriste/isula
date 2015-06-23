package isula.aco;

import isula.aco.exception.InvalidInputException;
import isula.aco.exception.MethodNotImplementedException;

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
   * @throws MethodNotImplementedException
   */
  public Environment(double[][] problemGraph) throws InvalidInputException {
    this.problemGraph = problemGraph;
    this.pheromoneMatrix = createPheromoneMatrix();

    if (!this.isProblemGraphValid()) {
      throw new InvalidInputException();
    }
  }

  protected double[][] createPheromoneMatrix()
      throws MethodNotImplementedException {
    throw new MethodNotImplementedException();
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

}
