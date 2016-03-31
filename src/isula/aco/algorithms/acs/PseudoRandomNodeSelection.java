package isula.aco.algorithms.acs;

import isula.aco.AntPolicy;
import isula.aco.AntPolicyType;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;
import isula.aco.algorithms.antsystem.RandomNodeSelection;
import isula.aco.exception.SolutionConstructionException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

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


    @Override
    public boolean applyPolicy(E environment, ConfigurationProvider configuration) {
        boolean nodeWasSelected = false;
        C nextNode = null;
        Random random = new Random();
        double randomValue = random.nextDouble();

        AcsConfigurationProvider configurationProvider = (AcsConfigurationProvider) configuration;

        double bestChoiceProbability = configurationProvider
                .getBestChoiceProbability();

        HashMap<C, Double> componentsWithProbabilities = this
                .getComponentsWithProbabilities(environment, configurationProvider);

        if (randomValue < bestChoiceProbability) {

            // TODO(cgavidia): This branch has testing pending.
            nextNode = getMostConvenient(componentsWithProbabilities);

            if (nextNode != null) {
                nodeWasSelected = true;
                getAnt().visitNode(nextNode);
            }

        } else {
            nodeWasSelected = super.applyPolicy(environment, configuration);
        }

        if (!nodeWasSelected) {
            nodeWasSelected = this.doIfNoNodeWasSelected(environment, configuration);
        }

        return nodeWasSelected;
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

        Iterator<Entry<C, Double>> componentWithProbabilitiesIterator = componentsWithProbabilities
                .entrySet().iterator();
        while (componentWithProbabilitiesIterator.hasNext()) {
            Entry<C, Double> componentWithProbability = componentWithProbabilitiesIterator
                    .next();

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
