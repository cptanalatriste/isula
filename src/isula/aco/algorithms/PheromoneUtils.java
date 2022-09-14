package isula.aco.algorithms;

import isula.aco.Ant;
import isula.aco.Environment;
import isula.aco.exception.ConfigurationException;

import java.util.List;
import java.util.function.IntToDoubleFunction;
import java.util.stream.IntStream;

public class PheromoneUtils {

    private PheromoneUtils() {

    }

    public static void validatePheromoneValue(double v) {
        if (Double.isInfinite(v) || Double.isNaN(v)) {
            throw new ConfigurationException("The pheromone value calculated is not a valid number: " +
                    v);
        }
    }

    public static <C, E extends Environment> void updatePheromoneForAntSolution(Ant<C, E> ant, E environment,
                                                                                IntToDoubleFunction pheromoneUpdater) {

        List<C> antSolution = ant.getSolution();
        IntStream.range(0, antSolution.size()).forEach(componentIndex -> {
            C solutionComponent = antSolution.get(componentIndex);
            Double newValue = pheromoneUpdater.applyAsDouble(componentIndex);

            if (newValue != Double.MIN_VALUE) {
                ant.setPheromoneTrailValue(solutionComponent, componentIndex, environment, newValue);
                validatePheromoneValue(ant.getPheromoneTrailValue(solutionComponent, componentIndex, environment));
            }
        });
    }
}
