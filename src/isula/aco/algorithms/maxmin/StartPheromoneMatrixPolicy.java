package isula.aco.algorithms.maxmin;

import isula.aco.ACOPhase;
import isula.aco.ACOPolicy;
import isula.aco.AntColony;
import isula.aco.Environment;

import java.util.logging.Logger;

//TODO(cgavidia): Maybe it is conveniente to have a Max-Min Policy base class.
public class StartPheromoneMatrixPolicy extends ACOPolicy {

  private static Logger logger = Logger
      .getLogger(StartPheromoneMatrixPolicy.class.getName());

  private MaxMinConfigurationProvider configurationProvider;

  public StartPheromoneMatrixPolicy(
      MaxMinConfigurationProvider configurationProvider) {
    super(ACOPhase.INITIAL_CONFIGURATION);

    this.configurationProvider = configurationProvider;
  }

  @Override
  public void applyPolicy(Environment environment, AntColony antColony) {

    double initialPheromoneValue = configurationProvider
        .getMaximumPheromoneValue();

    logger.info("Initial pheromone value: " + initialPheromoneValue);

    // TODO(cgavidia): Again, we shouldn't handle the data structure directly.

    double pheromoneMatrix[][] = environment.getPheromoneMatrix();
    int matrixRows = pheromoneMatrix.length;
    int matrixColumns = pheromoneMatrix.length;

    for (int i = 0; i < matrixRows; i++) {
      for (int j = 0; j < matrixColumns; j++) {
        pheromoneMatrix[i][j] = initialPheromoneValue;
      }
    }
  }

}
