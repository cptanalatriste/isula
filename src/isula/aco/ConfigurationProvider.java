package isula.aco;

public interface ConfigurationProvider {

  int getNumberOfAnts();

  double getEvaporationRatio();

  int getNumberOfIterations();

  double getInitialPheromoneValue();

}
