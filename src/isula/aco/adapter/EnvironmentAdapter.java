package isula.aco.adapter;

import isula.aco.Environment;
import isula.aco.exception.InvalidInputException;

/**
 * Environment implementation with default behaviour. On this type:
 * 
 * <ul>
 * <li>The pheromone matrix dimensions are the same as the problem graph.
 * </ul>
 * 
 * @author Carlos G. Gavidia (cgavidia@acm.org) 
 */
public class EnvironmentAdapter extends Environment {

  public EnvironmentAdapter(double[][] problemGraph)
      throws InvalidInputException {
    super(problemGraph);
  }

  /*
   * (non-Javadoc)
   * 
   * @see isula.aco.Environment#createPheromoneMatrix()
   */
  @Override
  protected double[][] createPheromoneMatrix() {
    return new double[this.getProblemGraph().length][this.getProblemGraph()[0].length];
  }
}
