package isula.aco.algorithms.maxmin;

import isula.aco.ConfigurationProvider;
import isula.aco.Environment;
import isula.aco.algorithms.antsystem.StartPheromoneMatrix;

/**
 * The initialization procedure for the Pheromone Matrix in a MMAS Algorithm. It
 * assigns the maximum allowed value to all cells.
 * 
 * @author Carlos G. Gavidia
 * 
 * @param <C>
 *          Class for components of a solution.
 */
public class StartPheromoneMatrixForMaxMin<C, E extends Environment> extends
    StartPheromoneMatrix<C, E> {
  // TODO(cgavidia): Maybe it is convenient to have a Max-Min Policy base class.

  @Override
  protected double getInitialPheromoneValue(ConfigurationProvider provider) {
    MaxMinConfigurationProvider configurationProvider = (MaxMinConfigurationProvider) provider;
    double initialPheromoneValue = configurationProvider
        .getMaximumPheromoneValue();

    return initialPheromoneValue;
  }

}
