package isula.aco.algorithms.antsystem;

import isula.aco.ConfigurationProvider;
import isula.aco.DaemonAction;
import isula.aco.DaemonActionType;
import isula.aco.algorithms.maxmin.StartPheromoneMatrixForMaxMin;

import java.util.logging.Logger;

/**
 * A simple initialization procedure for a Pheromone Matrix. It just assigns all
 * cells the value provided by Configuration.
 * 
 * @author Carlos G. Gavidia
 * 
 * @param <E>
 *          Class for components of a solution.
 */
public class StartPheromoneMatrix<E> extends DaemonAction<E> {

  private static Logger logger = Logger
      .getLogger(StartPheromoneMatrixForMaxMin.class.getName());

  public StartPheromoneMatrix() {
    super(DaemonActionType.INITIAL_CONFIGURATION);
  }

  @Override
  public void applyDaemonAction(ConfigurationProvider configurationProvider) {
    logger.info("INITIALIZING PHEROMONE MATRIX");

    double initialPheromoneValue = getInitialPheromoneValue(configurationProvider);
    logger.info("Initial pheromone value: " + initialPheromoneValue);

    getEnvironment().populatePheromoneMatrix(initialPheromoneValue);
  }

  protected double getInitialPheromoneValue(
      ConfigurationProvider configurationProvider) {
    return configurationProvider.getInitialPheromoneValue();
  }
}
