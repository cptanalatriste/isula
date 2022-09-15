package isula.aco.algorithms.antsystem;

import isula.aco.*;
import isula.aco.exception.ConfigurationException;
import isula.aco.exception.SolutionConstructionException;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This is the node selection policy used in Ant System algorithms, also known as Random Proportional Rule.
 *
 * @param <C> Class for components of a solution.
 * @param <E> Class representing the Environment.
 * @author Carlos G. Gavidia
 */
public class RandomNodeSelection<C, E extends Environment> extends
        AntPolicy<C, E> {

    private static Logger logger = Logger.getLogger(RandomNodeSelection.class.getName());


    public RandomNodeSelection() {
        super(AntPolicyType.NODE_SELECTION);
    }

    @Override
    public boolean applyPolicy(E environment, ConfigurationProvider configurationProvider) {

        logger.fine("Starting node selection");

        Map<C, Double> componentsWithProbabilities = this.getComponentsWithProbabilities(environment,
                configurationProvider);

        C nextNode = getNextComponent(componentsWithProbabilities);
        getAnt().visitNode(nextNode, environment);
        logger.fine("Ending node selection");
        return true;
    }

    public C getNextComponent(Map<C, Double> componentsWithProbabilities) {
        List<C> listOfComponents = new ArrayList<>(componentsWithProbabilities.keySet());
        List<Double> probabilities = listOfComponents
                .stream()
                .mapToDouble(componentsWithProbabilities::get)
                .boxed()
                .collect(Collectors.toList());

        int[] componentIndexes = IntStream.range(0, listOfComponents.size()).toArray();
        double[] arrayOfProbabilities = probabilities.stream().mapToDouble(probability -> probability).toArray();

        EnumeratedIntegerDistribution distribution = new EnumeratedIntegerDistribution(componentIndexes,
                arrayOfProbabilities);
        return listOfComponents.get(distribution.sample(1)[0]);
    }

    /**
     * Gets a probabilities vector, containing probabilities to move to each node
     * according to pheromone matrix.
     *
     * @param environment           Environment that ants are traversing.
     * @param configurationProvider Configuration provider.
     * @return Probabilities for the adjacent nodes.
     */
    public Map<C, Double> getComponentsWithProbabilities(E environment,
                                                         ConfigurationProvider configurationProvider) {

        List<C> neighborhood = getAnt().getNeighbourhood(environment);
        if (neighborhood == null) {
            throw new SolutionConstructionException("The ant's neighbourhood is null. There are no candidate " +
                    "components to add.");
        }

        return getProbabilitiesForNeighbourhood(environment, configurationProvider, neighborhood);
    }

    public Map<C, Double> getProbabilitiesForNeighbourhood(E environment, ConfigurationProvider configurationProvider,
                                                           List<C> neighborhood) {
        Map<C, Double> componentsWithProbabilities = neighborhood
                .stream()
                .unordered()
                .filter(component -> !getAnt().isNodeVisited(component) && getAnt().isNodeValid(component))
                .collect(Collectors.toMap(component -> component,
                        component -> getHeuristicTimesPheromone(environment, configurationProvider, component)));

        if (componentsWithProbabilities.size() < 1) {
            return doIfNoComponentsFound(environment, configurationProvider);
        }

        double sumOfMapValues = componentsWithProbabilities.values().stream().mapToDouble(value -> value).sum();

        componentsWithProbabilities
                .forEach((key, value) -> {
                    Double probabilityValue = value / sumOfMapValues;
                    if (probabilityValue.isNaN() || probabilityValue.isInfinite()) {
                        throw new ConfigurationException("The probability for component " + key +
                                " is not a valid number. Current value: " + probabilityValue + " (" + value
                                + "/" + sumOfMapValues + ")");
                    }

                    componentsWithProbabilities.put(key, probabilityValue);
                });

        double totalProbability = componentsWithProbabilities.values().stream().mapToDouble(value -> value).sum();

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
                        + "\n Partial solution: "
                        + getAnt().getSolution()
                        + " at position " + (getAnt().getCurrentIndex() - 1)
                        + "\n Environment: " + environment.toString()
                        + "\nPartial solution : " + getAnt().getSolutionAsString());
    }

    public Double getHeuristicTimesPheromone(E environment,
                                             ConfigurationProvider configurationProvider, C possibleMove) {


        Double heuristicValue = getAnt().getHeuristicValue(possibleMove, getAnt().getCurrentIndex(), environment);
        Double pheromoneTrailValue = getAnt().getPheromoneTrailValue(possibleMove, getAnt().getCurrentIndex(),
                environment);

        if (heuristicValue == null || heuristicValue.isNaN() || heuristicValue.isInfinite() || pheromoneTrailValue == null
                || pheromoneTrailValue.isNaN() || pheromoneTrailValue.isInfinite()) {

            throw new SolutionConstructionException("The current ant is not producing valid pheromone/heuristic values" +
                    " for the solution component: " + possibleMove + " . Heuristic value " + heuristicValue +
                    " Pheromone value: " + pheromoneTrailValue);
        }

        return Math.pow(heuristicValue, configurationProvider.getHeuristicImportance())
                * Math.pow(pheromoneTrailValue, configurationProvider.getPheromoneImportance());
    }

    @Override
    public String toString() {
        return "RandomNodeSelection{}";
    }
}
