package isula.aco.algorithms.antsystem;

import isula.aco.AntPolicy;
import isula.aco.AntPolicyType;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;

/**
 * The pheromone update policy of Ant System. After an Ant has built a solution,
 * pheromone is deposited in each of the solution components.
 * 
 * @author Carlos G. Gavidia
 * 
 * @param <E>
 *          Class for components of a solution.
 */
public abstract class OnlinePheromoneUpdate<E> extends AntPolicy<E> {

  public OnlinePheromoneUpdate() {
    super(AntPolicyType.AFTER_SOLUTION_IS_READY);
  }

  @Override
  public void applyPolicy(Environment environment,
      ConfigurationProvider configurationProvider) {

    for (int i = 0; i < getAnt().getSolution().length; i++) {
      E solutionComponent = getAnt().getSolution()[i];
      double newPheromoneValue = this.getNewPheromoneValue(solutionComponent,
          environment, configurationProvider);
      getAnt().setPheromoneTrailValue(solutionComponent, environment,
          newPheromoneValue);
    }
  }

  protected abstract double getNewPheromoneValue(E solutionComponent,
      Environment environment, ConfigurationProvider configurationProvider);

}
