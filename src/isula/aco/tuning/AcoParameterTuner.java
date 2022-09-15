package isula.aco.tuning;

import isula.aco.ConfigurationProvider;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcoParameterTuner {

    private static Logger logger = Logger.getLogger(AcoParameterTuner.class
            .getName());

    private List<Integer> numberOfAntsValues;
    private List<Double> evaporationRatioValues;
    private List<Integer> numberOfIterationValues;
    private List<Double> initialPheromoneValues;
    private List<Double> heuristicImportanceValues;
    private List<Double> pheromoneImportanceValues;

    private BasicConfigurationProvider optimalConfiguration;

    private double executionTime = 0.0;
    private Double bestSolutionCost = null;


    public AcoParameterTuner(List<Integer> numberOfAntsValues, List<Double> evaporationRatioValues,
                             List<Integer> numberOfIterationValues, List<Double> initialPheromoneValues,
                             List<Double> heuristicImportanceValues, List<Double> pheromoneImportanceValues) {
        this.numberOfAntsValues = numberOfAntsValues;
        this.evaporationRatioValues = evaporationRatioValues;
        this.numberOfIterationValues = numberOfIterationValues;
        this.initialPheromoneValues = initialPheromoneValues;
        this.heuristicImportanceValues = heuristicImportanceValues;
        this.pheromoneImportanceValues = pheromoneImportanceValues;

        optimalConfiguration = new BasicConfigurationProvider();

        optimalConfiguration.setNumberOfAnts(numberOfAntsValues.get(0));
        optimalConfiguration.setEvaporationRatio(evaporationRatioValues.get(0));
        optimalConfiguration.setNumberOfIterations(numberOfIterationValues.get(0));
        optimalConfiguration.setInitialPheromoneValue(initialPheromoneValues.get(0));
        optimalConfiguration.setHeuristicImportance(heuristicImportanceValues.get(0));
        optimalConfiguration.setPheromoneImportance(pheromoneImportanceValues.get(0));

    }

    public ConfigurationProvider getOptimalConfiguration(ParameterOptimisationTarget optimisationTarget) {

        logger.log(Level.INFO, "Starting computation at: {0}", new Date());
        final long startTime = System.nanoTime();

        this.setOptimalValue(optimisationTarget, optimalConfiguration::setNumberOfAnts,
                this.numberOfAntsValues);

        this.setOptimalValue(optimisationTarget, optimalConfiguration::setEvaporationRatio,
                this.evaporationRatioValues);

        this.setOptimalValue(optimisationTarget, optimalConfiguration::setNumberOfIterations,
                this.numberOfIterationValues);

        this.setOptimalValue(optimisationTarget, optimalConfiguration::setInitialPheromoneValue,
                this.initialPheromoneValues);

        this.setOptimalValue(optimisationTarget, optimalConfiguration::setHeuristicImportance,
                this.heuristicImportanceValues);

        this.setOptimalValue(optimisationTarget, optimalConfiguration::setPheromoneImportance,
                this.pheromoneImportanceValues);

        this.bestSolutionCost = optimisationTarget.getSolutionCost(this.optimalConfiguration);

        logger.log(Level.INFO, "Finishing computation at: {0}", new Date());
        long endTime = System.nanoTime();
        executionTime = (endTime - startTime) / 1000000000.0;
        logger.log(Level.INFO, "Duration (in seconds): {0}", executionTime);

        logger.log(Level.INFO, "Optimal configuration: {0}", this.optimalConfiguration);
        logger.log(Level.INFO, "Final solution cost after parameter tuning: {0}", bestSolutionCost);

        return this.optimalConfiguration;

    }


    private <T> Double setOptimalValue(ParameterOptimisationTarget optimisationTarget, Consumer<T> parameterSetter,
                                       List<T> parameterValues) {
        if (parameterValues.size() == 1) {
            parameterSetter.accept(parameterValues.get(0));
            return null;
        }

        Double bestCost = null;
        T bestParameterValue = null;

        for (T parameterValue : parameterValues) {
            parameterSetter.accept(parameterValue);

            double currentSolutionCost = optimisationTarget.getSolutionCost(this.optimalConfiguration);
            if (bestCost == null || bestParameterValue == null || bestCost > currentSolutionCost) {
                bestCost = currentSolutionCost;
                bestParameterValue = parameterValue;
            }
        }

        parameterSetter.accept(bestParameterValue);

        return bestCost;
    }

    @Override
    public String toString() {
        return "AcoParameterTuner{" +
                "numberOfAntsValues=" + numberOfAntsValues +
                ", evaporationRatioValues=" + evaporationRatioValues +
                ", numberOfIterationValues=" + numberOfIterationValues +
                ", initialPheromoneValues=" + initialPheromoneValues +
                ", heuristicImportanceValues=" + heuristicImportanceValues +
                ", pheromoneImportanceValues=" + pheromoneImportanceValues +
                ", optimalConfiguration=" + optimalConfiguration +
                ", executionTime=" + executionTime +
                ", bestSolutionCost=" + bestSolutionCost +
                '}';
    }
}
