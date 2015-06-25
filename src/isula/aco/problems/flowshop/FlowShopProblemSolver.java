package isula.aco.problems.flowshop;

import isula.aco.AcoProblemSolver;
import isula.aco.AntColony;
import isula.aco.ConfigurationProvider;
import isula.aco.exception.InvalidInputException;

public class FlowShopProblemSolver extends AcoProblemSolver<Integer> {

  /**
   * Problem Solver for the Flow Shop Scheduling Problem.
   * 
   * @param problemGraph
   *          Graph representation for the problem.
   * @param configurationProvider
   *          Configuration Provider.
   * @throws InvalidInputException
   *           When the problem graph is incorrectly formed.
   */
  public FlowShopProblemSolver(double[][] problemGraph,
      ConfigurationProvider configurationProvider) throws InvalidInputException {
    FlowShopEnvironment environment = new FlowShopEnvironment(problemGraph);

    int numberOfAnts = configurationProvider.getNumberOfAnts();
    int numberOfJobs = environment.getNumberOfJobs();
    AntColony<Integer> antColony = new FlowShopAntColony(numberOfAnts,
        numberOfJobs);

    // TODO(cgavidia); This should be called from the base classes, not from the
    // specific ones.
    antColony.buildColony();

    this.setConfigurationProvider(configurationProvider);
    this.setEnvironment(environment);
    this.setAntColony(antColony);
  }
}
