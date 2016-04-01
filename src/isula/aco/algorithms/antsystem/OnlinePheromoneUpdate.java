package isula.aco.algorithms.antsystem;

import isula.aco.AntPolicy;
import isula.aco.AntPolicyType;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;

/**
 * The pheromone update policy of Ant System. After an Ant has built a solution,
 * pheromone is deposited in each of the solution components.
 *
 * @param <C> Class for components of a solution.
 * @param <E> Class representing the Environment.
 * @author Carlos G. Gavidia
 */
public abstract class OnlinePheromoneUpdate<C, E extends Environment> extends
        AntPolicy<C, E> {

    public OnlinePheromoneUpdate() {
        super(AntPolicyType.AFTER_SOLUTION_IS_READY);
    }

    @Override
    public boolean applyPolicy(E environment,
                               ConfigurationProvider configurationProvider) {

        for (int i = 0; i < getAnt().getSolution().length; i++) {
            C solutionComponent = getAnt().getSolution()[i];
            double newPheromoneValue = this.getNewPheromoneValue(solutionComponent, i,
                    environment, configurationProvider);
            getAnt().setPheromoneTrailValue(solutionComponent, i, environment,
                    newPheromoneValue);
        }

        return true;
    }

    /**
     * Calculates the pheromone value to be used in the pheromone matrix update.
     *
     * @param positionInSolution    Position of the component in the solution.
     * @param solutionComponent     Solution component.
     * @param environment           Environment with problem specific information.
     * @param configurationProvider Algorithm configuration.
     * @return New pheromone value.
     */
    protected abstract double getNewPheromoneValue(C solutionComponent,
                                                   Integer positionInSolution,
                                                   E environment, ConfigurationProvider configurationProvider);

}
