package isula.aco.algorithms.antsystem;

import isula.aco.*;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static isula.aco.algorithms.PheromoneUtils.updatePheromoneForAntSolution;

/**
 * Update pheromone process triggered after every ant has build a feasible solution. This action only considers the
 * increment caused by pheromone deposit.
 *
 * @param <C> Class for components of a solution.
 * @param <E> Class representing the Environment.
 * @author Carlos G. Gavidia
 */
public class OfflinePheromoneUpdate<C, E extends Environment> extends DaemonAction<C, E> {

    private static Logger logger = Logger.getLogger(OfflinePheromoneUpdate.class.getName());

    public OfflinePheromoneUpdate() {
        super(DaemonActionType.AFTER_ITERATION_CONSTRUCTION);
    }

    @Override
    public void applyDaemonAction(ConfigurationProvider configurationProvider) {

        logger.log(Level.FINE, "Performing offline pheromone update");

        E environment = getEnvironment();

        getAntColony().getHive().forEach((ant) -> updatePheromoneForAntSolution(ant, getEnvironment(), (positionInSolution) -> {
            C solutionComponent = ant.getSolution()[positionInSolution];

            if (solutionComponent != null) {

                double originalPheromoneValue = ant.getPheromoneTrailValue(solutionComponent, positionInSolution,
                        environment);

                double pheromoneDeposit = this.getPheromoneDeposit(ant, positionInSolution, solutionComponent,
                        environment, configurationProvider);

                return originalPheromoneValue + pheromoneDeposit;
            }
            return null;
        }));

        logger.fine("Pheromone matrix after update :" + Arrays.deepToString(environment.getPheromoneMatrix()));
    }

    /**
     * Calculates the pheromone value to be used in the pheromone matrix update.
     *
     * @param ant                   Ant instance doing the update.
     * @param positionInSolution    Position of the component in the solution.
     * @param solutionComponent     Solution component.
     * @param environment           Environment with problem specific information.
     * @param configurationProvider Algorithm configuration.
     * @return New pheromone value.
     */
    protected double getPheromoneDeposit(Ant<C, E> ant,
                                         Integer positionInSolution, C solutionComponent,
                                         E environment, ConfigurationProvider configurationProvider) {
        AntSystemConfigurationProvider antSystemConfiguration = (AntSystemConfigurationProvider) configurationProvider;
        return antSystemConfiguration.getPheromoneDepositFactor() / ant.getSolutionCost(environment);
    }

    @Override
    public String toString() {
        return "OfflinePheromoneUpdate{}";
    }
}
