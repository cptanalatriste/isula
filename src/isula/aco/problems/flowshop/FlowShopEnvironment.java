package isula.aco.problems.flowshop;

import isula.aco.Environment;
import isula.aco.exception.InvalidInputException;
import isula.aco.exception.MethodNotImplementedException;

import java.util.logging.Logger;

public class FlowShopEnvironment extends Environment {

  private static Logger logger = Logger.getLogger(FlowShopEnvironment.class
      .getName());

  private int numberOfJobs;

  /**
   * Environment for the Flow Shop Scheduling Problem.
   * 
   * @param problemGraph
   *          Graph representation of the problem.
   * @throws InvalidInputException
   */
  public FlowShopEnvironment(double[][] problemGraph)
      throws InvalidInputException {
    super(problemGraph);
    this.numberOfJobs = problemGraph.length;

    logger.info("Number of Jobs: " + numberOfJobs);
  }

  public int getNumberOfJobs() {
    return getProblemGraph().length;
  }

  @Override
  protected boolean isProblemGraphValid() {
    int numberOfMachines = getProblemGraph()[0].length;
    int jobs = getNumberOfJobs();

    for (int i = 1; i < jobs; i++) {
      if (getProblemGraph()[i].length != numberOfMachines) {
        return false;
      }
    }

    logger.info("Number of Machines: " + numberOfMachines);
    return true;
  }

  @Override
  protected double[][] createPheromoneMatrix()
      throws MethodNotImplementedException {
    int jobs = getNumberOfJobs();

    return new double[jobs][jobs];
  }

}
