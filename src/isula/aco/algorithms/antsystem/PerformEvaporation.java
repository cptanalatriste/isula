package isula.aco.algorithms.antsystem;

import isula.aco.ConfigurationProvider;
import isula.aco.DaemonAction;
import isula.aco.DaemonActionType;
import isula.aco.Environment;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple evaporation policy taken from Ant System. It simply applies the
 * evaporation ratio to all elements on the Pheromone Matrix.
 *
 * @param <C> Class for components of a solution.
 * @param <E> Class representing the Environment.
 * @author Carlos G. Gavidia
 */
public class PerformEvaporation<C, E extends Environment> extends
        DaemonAction<C, E> {

    private static Logger logger = Logger.getLogger(PerformEvaporation.class
            .getName());

    public PerformEvaporation() {
        super(DaemonActionType.AFTER_ITERATION_CONSTRUCTION);
    }

    @Override
    public void applyDaemonAction(ConfigurationProvider configurationProvider) {
        double evaporationRatio = configurationProvider.getEvaporationRatio();

        logger.fine("Performing evaporation on all edges");
        logger.log(Level.FINE, "Evaporation ratio: {0}", evaporationRatio);

        getEnvironment().applyFactorToPheromoneMatrix(evaporationRatio);
    }

    @Override
    public String toString() {
        return "PerformEvaporation{}";
    }
}
