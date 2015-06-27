package isula.aco.algorithms.maxmin;

import isula.aco.ConfigurationProvider;
import isula.aco.algorithms.antsystem.StartPheromoneMatrix;

//TODO(cgavidia): Maybe it is convenient to have a Max-Min Policy base class.
public class StartPheromoneMatrixForMaxMin<E> extends StartPheromoneMatrix<E> {

  @Override
  protected double getInitialPheromoneValue(ConfigurationProvider provider) {
    MaxMinConfigurationProvider configurationProvider = (MaxMinConfigurationProvider) provider;
    double initialPheromoneValue = configurationProvider
        .getMaximumPheromoneValue();

    return initialPheromoneValue;
  }

}
