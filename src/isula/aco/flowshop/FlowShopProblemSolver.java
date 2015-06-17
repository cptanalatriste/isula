package isula.aco.flowshop;

import isula.aco.ACOProblemSolver;
import isula.aco.AntColony;
import isula.aco.ConfigurationProvider;
import isula.aco.exception.InvalidInputException;
import isula.aco.exception.MethodNotImplementedException;

public class FlowShopProblemSolver extends ACOProblemSolver {

  public FlowShopProblemSolver(double[][] problemGraph,
      ConfigurationProvider configurationProvider)
      throws InvalidInputException, MethodNotImplementedException {
    FlowShopEnvironment environment = new FlowShopEnvironment(problemGraph);

    int numberOfAnts = configurationProvider.getNumberOfAnts();
    int numberOfJobs = environment.getNumberOfJobs();
    AntColony antColony = new FlowShopAntColony(numberOfAnts, numberOfJobs);

    // TODO(cgavidia); This should be called from the base classes, not from the
    // specific ones.
    antColony.buildColony();

    this.setConfigurationProvider(configurationProvider);
    this.setEnvironment(environment);
    this.setAntColony(antColony);
  }

}
