package isula.aco.algorithms.antsystem;

import isula.aco.ConfigurationProvider;
import isula.aco.DaemonAction;
import isula.aco.DaemonActionType;

public class BaseStartPheromoneMatrix<E> extends DaemonAction<E> {

  public BaseStartPheromoneMatrix() {
    super(DaemonActionType.INITIAL_CONFIGURATION);
  }

  @Override
  public void applyDaemonAction(ConfigurationProvider configurationProvider) {
    getEnvironment().populatePheromoneMatrix(
        getInitialPheromoneValue(configurationProvider));
  }

  protected double getInitialPheromoneValue(
      ConfigurationProvider configurationProvider) {
    double initialPheromoneValue = configurationProvider
        .getInitialPheromoneValue();

    return initialPheromoneValue;
  }
}
