package isula.aco.problems.flowshop;

import isula.aco.ACOProblemSolver;
import isula.aco.AntColony;
import isula.aco.ConfigurationProvider;
import isula.aco.algorithms.maxmin.MaxMinConfigurationProvider;
import isula.aco.algorithms.maxmin.StartPheromoneMatrixPolicy;
import isula.aco.algorithms.maxmin.UpdatePheromoneMatrixPolicy;
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

  // TODO(cgavidia): Temporarly, we're calling theses method externally. It
  // should
  // be called internally.
  @Override
  public void applyInitialConfigurationPolicies() {
    StartPheromoneMatrixPolicy startPheromoneMatrixPolicy = new StartPheromoneMatrixPolicy(
        (MaxMinConfigurationProvider) this.getConfigurationProvider());
    startPheromoneMatrixPolicy.applyPolicy(this.getEnvironment(),
        this.getAntColony());
  }

  // TODO(cgavidia): The policies are hard coded here and before. We should have
  // lists of policies per problem-solver.
  @Override
  public void applyAfterSolutionConstructionPolicies() {
    UpdatePheromoneMatrixPolicy updatePheromoneMatrixPolicy = new UpdatePheromoneMatrixPolicy(
        (MaxMinConfigurationProvider) this.getConfigurationProvider());
    updatePheromoneMatrixPolicy.applyPolicy(this.getEnvironment(),
        this.getAntColony());

  }

}
