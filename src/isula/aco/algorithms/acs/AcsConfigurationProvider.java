package isula.aco.algorithms.acs;

/**
 * A configuration provider, but for Ant Colony System algorithm.
 * 
 * @author Carlos G. Gavidia
 * 
 */
public interface AcsConfigurationProvider {

  double getBestChoiceProbability();

  double getHeuristicImportance();

  double getPheromoneImportance();

}
