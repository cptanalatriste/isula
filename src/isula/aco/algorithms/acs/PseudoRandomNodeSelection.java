package isula.aco.algorithms.acs;

import isula.aco.ConfigurationProvider;
import isula.aco.Environment;
import isula.aco.algorithms.antsystem.RandomNodeSelection;
import isula.aco.exception.SolutionConstructionException;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Logger;

/**
 * The selection policy for Ant Colony System algorithms. It defines to
 * selection rules.
 * <p>
 * <ul>
 * <li>The first is the best selection rule. It picks the component with the
 * better heuristic and pheromone information.
 * <li>The other one is the usual ACO policy, selecting based on the
 * probabilities of each possible component.
 * <li>
 * </ul>
 * <p>
 * <p>
 * Using one or another is determined by a "best-choice" probability.
 *
 * @param <C> Class for components of a solution.
 * @param <E> Class representing the Environment.
 * @author Carlos G. Gavidia
 */
public class PseudoRandomNodeSelection<C, E extends Environment> extends
        RandomNodeSelection<C, E> {

    private static Logger logger = Logger.getLogger(PseudoRandomNodeSelection.class.getName());


    @Override
    public boolean applyPolicy(E environment, ConfigurationProvider configuration) {
        boolean nodeWasSelected = false;
        C nextNode;
        AcsConfigurationProvider configurationProvider = (AcsConfigurationProvider) configuration;

        HashMap<C, Double> componentsWithProbabilities = this
                .getComponentsWithProbabilities(environment, configurationProvider);

        if (selectMostConvenient(configurationProvider)) {
            logger.fine("Selecting the greedy choice");

            nextNode = getMostConvenient(componentsWithProbabilities);

            if (nextNode != null) {
                nodeWasSelected = true;
                getAnt().visitNode(nextNode, environment);
            }

        } else {
            logger.fine("Selecting the probabilistic choice");

            nodeWasSelected = super.applyPolicy(environment, configuration);
        }

        if (!nodeWasSelected) {
            nodeWasSelected = this.doIfNoNodeWasSelected(environment, configuration);
        }

        return nodeWasSelected;
    }

    /**
     * In a pseudo-random selection rule, determines if we greedily select the most convenient component.
     *
     * @param configurationProvider Algorithm configuration.
     * @return True if the greedy approach is followed, false otherwise.
     */
    protected boolean selectMostConvenient(AcsConfigurationProvider configurationProvider) {
        double bestChoiceProbability = configurationProvider
                .getBestChoiceProbability();
        Random random = new Random();
        double randomValue = random.nextDouble();
        return randomValue < bestChoiceProbability;
    }

    /**
     * Returns the most convenient component based on heuristic and pheromone
     * information.
     *
     * @param componentsWithProbabilities Possible components.
     * @return Most convenient component.
     */
    public C getMostConvenient(HashMap<C, Double> componentsWithProbabilities) {
        C nextNode = null;
        double currentMaximumProbability = -1;

        for (Entry<C, Double> componentWithProbability : componentsWithProbabilities
                .entrySet()) {
            C possibleMove = componentWithProbability.getKey();
            double currentProbability = componentWithProbability.getValue();

            if (!getAnt().isNodeVisited(possibleMove)
                    && currentProbability > currentMaximumProbability) {

                nextNode = possibleMove;
                currentMaximumProbability = currentProbability;
            }
        }
        return nextNode;
    }

    protected boolean doIfNoNodeWasSelected(E environment,
                                            ConfigurationProvider configuration) {
        throw new SolutionConstructionException(
                "This policy couldn't select a new component for the current solution. \n"
                        + "Partial solution is: " + getAnt().getSolutionAsString());
    }


}
