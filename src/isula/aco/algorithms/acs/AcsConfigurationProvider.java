package isula.aco.algorithms.acs;

public interface AcsConfigurationProvider {

  double getBestChoiceProbability();

  double getHeuristicImportance();

  double getPheromoneImportance();

}
