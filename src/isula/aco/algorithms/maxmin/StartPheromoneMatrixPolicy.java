package isula.aco.algorithms.maxmin;

import isula.aco.ConfigurationProvider;
import isula.aco.DaemonAction;
import isula.aco.DaemonActionType;

import java.util.logging.Logger;

//TODO(cgavidia): Maybe it is convenient to have a Max-Min Policy base class.
public class StartPheromoneMatrixPolicy extends DaemonAction {

  private static Logger logger = Logger
      .getLogger(StartPheromoneMatrixPolicy.class.getName());

  /**
   * Instantiates the Start Pheromone Matrix Policy.
   * 
   * @param configurationProvider
   *          Configuration provider.
   */
  public StartPheromoneMatrixPolicy() {
    super(DaemonActionType.INITIAL_CONFIGURATION);

  }

  @Override
  public void applyDaemonAction(ConfigurationProvider provider) {
    logger.info("INITIALIZING PHEROMONE MATRIX");

    MaxMinConfigurationProvider configurationProvider = (MaxMinConfigurationProvider) provider;
    double initialPheromoneValue = configurationProvider
        .getMaximumPheromoneValue();

    logger.info("Initial pheromone value: " + initialPheromoneValue);

    // TODO(cgavidia): Again, we shouldn't handle the data structure directly.

    double[][] pheromoneMatrix = getEnvironment().getPheromoneMatrix();
    int matrixRows = pheromoneMatrix.length;
    int matrixColumns = pheromoneMatrix.length;

    for (int i = 0; i < matrixRows; i++) {
      for (int j = 0; j < matrixColumns; j++) {
        pheromoneMatrix[i][j] = initialPheromoneValue;
      }
    }
  }

}
