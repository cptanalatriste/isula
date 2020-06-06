package isula.aco.algorithms.iteratedants;

import isula.aco.AntPolicy;
import isula.aco.AntPolicyType;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;
import isula.aco.exception.SolutionConstructionException;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class ConstructPartialSolution<C, E extends Environment> extends AntPolicy<C, E> {

    private static Logger logger = Logger.getLogger(ConstructPartialSolution.class
            .getName());

    public ConstructPartialSolution() {
        super(AntPolicyType.AFTER_SOLUTION_IS_READY);
    }

    @Override
    public boolean applyPolicy(E environment, ConfigurationProvider configurationProvider) {
        AntWithPartialSolution<C, E> ant = (AntWithPartialSolution<C, E>) getAnt();
        Collection<C> newPartialSolution = this.constructPartialSolution();
        ant.setPartialSolution(newPartialSolution);
        return true;
    }

    public abstract int getNumberOfComponentsToRemove();

    public Collection<C> constructPartialSolution() {
        AntWithPartialSolution<C, E> ant = (AntWithPartialSolution<C, E>) this.getAnt();
        E environment = ant.getEnvironment();

        if (!ant.isSolutionReady(environment)) {
            throw new SolutionConstructionException("Solution must be completed before obtaining partial solution");
        }

        List<Integer> indexesForRemoval = getComponentIndexesForRemoval(ant, getNumberOfComponentsToRemove());
        logger.info("indexesForRemoval: " + indexesForRemoval);
        return getNewPartialSolution(indexesForRemoval);
    }

    public abstract List<C> getNewPartialSolution(List<Integer> indexesForRemoval);

    private List<Integer> getComponentIndexesForRemoval(AntWithPartialSolution<C, E> ant,
                                                        int removedComponentNumber) {
        List<C> currentSolution = ant.getSolution();
        int[] componentIndexes = IntStream.range(0, currentSolution.size()).toArray();

        List<Double> pheromoneValues = IntStream.range(0, currentSolution.size()).
                mapToDouble((solutionIndex) -> {
                    C solutionComponent = currentSolution.get(solutionIndex);
                    return ant.getPheromoneTrailValue(solutionComponent, solutionIndex, ant.getEnvironment());
                })
                .boxed()
                .collect(Collectors.toList());

        Double totalPheromone = pheromoneValues.stream().reduce(0.0, Double::sum);
        List<Double> discreteProbabilities = pheromoneValues.stream()
                .mapToDouble((pheromonePerComponent) -> pheromonePerComponent / totalPheromone)
                .boxed()
                .collect(Collectors.toList());

        double[] arrayOfProbabilities = discreteProbabilities.stream()
                .mapToDouble((probability) -> probability)
                .toArray();

        double sumOfProbabilities = Arrays.stream(arrayOfProbabilities).sum();
        if (Math.abs(sumOfProbabilities - 1.0) > 0.001) {
            throw new SolutionConstructionException("Sum of probabilities should be one. Current value: " +
                    sumOfProbabilities);
        }


        EnumeratedIntegerDistribution distribution = new EnumeratedIntegerDistribution(componentIndexes,
                arrayOfProbabilities);

        int preservedComponentNumber = currentSolution.size() - removedComponentNumber;
        List<Integer> componentsToPreserve = Arrays.stream(distribution.sample(preservedComponentNumber))
                .boxed()
                .collect(Collectors.toList());

        return Arrays.stream(componentIndexes)
                .filter((componentIndex) -> !componentsToPreserve.contains(componentIndex))
                .boxed()
                .collect(Collectors.toList());
    }

}
