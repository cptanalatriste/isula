package isula.aco.algorithms.maxmin;

import isula.aco.ConfigurationProvider;

public interface MaxMinConfigurationProvider extends ConfigurationProvider {

  double getMaximumPheromoneValue();

  double getMinimumPheromoneValue();

}
