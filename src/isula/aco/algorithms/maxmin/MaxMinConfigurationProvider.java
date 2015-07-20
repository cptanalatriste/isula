package isula.aco.algorithms.maxmin;

import isula.aco.ConfigurationProvider;

/**
 * Configuration parameters of a Max-Min Ant System algorithm.
 * 
 * @author Carlos G. Gavidia
 *
 */
public interface MaxMinConfigurationProvider extends ConfigurationProvider {

  double getMaximumPheromoneValue();

  double getMinimumPheromoneValue();
}
