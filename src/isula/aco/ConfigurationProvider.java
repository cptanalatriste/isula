package isula.aco;

/**
 * Classes that implement this type provide configuration information to the
 * Problem Solvers. This interface contain parameters used by almost all ACO
 * algorithms.
 * 
 * @author Carlos G. Gavidia
 * 
 */
public interface ConfigurationProvider {

  int getNumberOfAnts();

  double getEvaporationRatio();

  int getNumberOfIterations();

  double getInitialPheromoneValue();

}
