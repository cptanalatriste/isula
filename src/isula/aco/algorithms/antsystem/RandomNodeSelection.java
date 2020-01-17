package isula.aco.algorithms.antsystem;

import isula.aco.AntPolicy;
import isula.aco.AntPolicyType;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;
import isula.aco.exception.ConfigurationException;
import isula.aco.exception.SolutionConstructionException;

import java.util.*;

/**
 * This is the node selection policy used in Ant System algorithms, also known as Random Proportional Rule.
 *
 * @param <C> Class for components of a solution.
 * @param <E> Class representing the Environment.
 * @author Carlos G. Gavidia
 */
public class RandomNodeSelection<C, E extends Environment> extends
        AntPolicy<C, E> {

    public RandomNodeSelection() {
        super(AntPolicyType.NODE_SELECTION);
    }

    @Override
    public boolean applyPolicy(E environment, ConfigurationProvider configurationProvider) {
        Random random = new Random();

        double value = random.nextDouble();
        double total = 0;

        HashMap<C, Double> componentsWithProbabilities = this
                .getComponentsWithProbabilities(environment, configurationProvider);
        Iterator<Map.Entry<C, Double>> componentWithProbabilitiesIterator = componentsWithProbabilities
                .entrySet().iterator();
        while (componentWithProbabilitiesIterator.hasNext()) {
            Map.Entry<C, Double> componentWithProbability = componentWithProbabilitiesIterator
                    .next();

            Double probability = componentWithProbability.getValue();
            if (probability.isNaN()) {
                throw new ConfigurationException("The probability for component " + componentWithProbability.getKey() +
                        " is not a number.");
            }

            total += probability;

            if (total >= value) {
                C nextNode = componentWithProbability.getKey();
                getAnt().visitNode(nextNode);
                return true;
            }
        }

        return false;
    }

    /**
     * Gets a probabilities vector, containing probabilities to move to each node
     * according to pheromone matrix.
     *
     * @param environment           Environment that ants are traversing.
     * @param configurationProvider Configuration provider.
     * @return Probabilities for the adjacent nodes.
     */
    public HashMap<C, Double> getComponentsWithProbabilities(E environment,
                                                             ConfigurationProvider configurationProvider) {
        HashMap<C, Double> componentsWithProbabilities = new HashMap<C, Double>();

        double denominator = Double.MIN_VALUE;

        List<C> neighborhood = getAnt().getNeighbourhood(environment);
        if (neighborhood == null) {
            throw new SolutionConstructionException("The ant's neighbourhood is null. There are no candidate " +
                    "components to add.");
        }

        for (C possibleMove : getAnt().getNeighbourhood(environment)) {

            if (!getAnt().isNodeVisited(possibleMove)
                    && getAnt().isNodeValid(possibleMove)) {

                Double heuristicTimesPheromone = getHeuristicTimesPheromone(
                        environment, configurationProvider, possibleMove);

                denominator += heuristicTimesPheromone;
                componentsWithProbabilities.put(possibleMove, 0.0);
            }
        }

        Double totalProbability = 0.0;
        Iterator<Map.Entry<C, Double>> componentWithProbabilitiesIterator = componentsWithProbabilities
                .entrySet().iterator();
        while (componentWithProbabilitiesIterator.hasNext()) {
            Map.Entry<C, Double> componentWithProbability = componentWithProbabilitiesIterator
                    .next();
            C component = componentWithProbability.getKey();

            Double numerator = getHeuristicTimesPheromone(environment,
                    configurationProvider, component);
            Double probability = numerator / denominator;
            totalProbability += probability;

            if (probability.isNaN() || probability.isInfinite()) {
                throw new ConfigurationException("The probability for component " + componentWithProbability.getKey() +
                        " is not a valid number. Current value: " + probability + " (" + numerator + "/" + denominator +
                        ")");
            }

            componentWithProbability.setValue(probability);
        }

        if (componentsWithProbabilities.size() < 1) {
            return doIfNoComponentsFound(environment, configurationProvider);
        }

        double delta = 0.001;
        if (Math.abs(totalProbability - 1.0) > delta) {
            throw new ConfigurationException("The sum of probabilities for the possible components is " +
                    totalProbability + ". We expect this value to be closer to 1.");
        }

        return componentsWithProbabilities;
    }


    protected HashMap<C, Double> doIfNoComponentsFound(E environment,
                                                       ConfigurationProvider configurationProvider) {
        throw new SolutionConstructionException(
                "We have no suitable components to add to the solution from current position."
                        + "\n Previous Component: "
                        + getAnt().getSolution()[getAnt().getCurrentIndex() - 1]
                        + " at position " + (getAnt().getCurrentIndex() - 1)
                        + "\n Environment: " + environment.toString()
                        + "\nPartial solution : " + getAnt().getSolutionAsString());
    }

    private Double getHeuristicTimesPheromone(E environment,
                                              ConfigurationProvider configurationProvider, C possibleMove) {


        Double heuristicValue = getAnt().getHeuristicValue(possibleMove,
                getAnt().getCurrentIndex(), environment);
        Double pheromoneTrailValue = getAnt().getPheromoneTrailValue(possibleMove,
                getAnt().getCurrentIndex(), environment);

        if (heuristicValue == null || heuristicValue.isNaN() || heuristicValue.isInfinite() || pheromoneTrailValue == null
                || pheromoneTrailValue.isNaN() || pheromoneTrailValue.isInfinite()) {

            throw new SolutionConstructionException("The current ant is not producing valid pheromone/heuristic values" +
                    " for the solution component: " + possibleMove + " . Heuristic value " + heuristicValue +
                    " Pheromone value: " + pheromoneTrailValue);
        }

        double result = Math.pow(heuristicValue, configurationProvider.getHeuristicImportance())
                * Math.pow(pheromoneTrailValue, configurationProvider.getPheromoneImportance());

        return result;
    }
}
