package isula.aco.tuning;

import isula.aco.AcoProblemSolver;
import isula.aco.ConfigurationProvider;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class AcoParameterTuner {

    private static Logger logger = Logger.getLogger(AcoProblemSolver.class
            .getName());

    private List<Integer> numberOfAntsValues;
    private List<Double> evaporationRatioValues;
    private List<Integer> numberOfIterationValues;
    private List<Double> initialPheromoneValues;
    private List<Double> heuristicImportanceValues;
    private List<Double> pheromoneImportanceValues;

    private BasicConfigurationProvider optimalConfiguration;

    private double executionTime = 0.0;
    private double bestSolutionCost = 0.0;


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

        logger.info("Starting computation at: " + new Date());
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

        bestSolutionCost = this.setOptimalValue(optimisationTarget, optimalConfiguration::setPheromoneImportance,
                this.pheromoneImportanceValues);

        logger.info("Finishing computation at: " + new Date());
        long endTime = System.nanoTime();
        executionTime = (endTime - startTime) / 1000000000.0;
        logger.info("Duration (in seconds): " + executionTime);

        logger.info("Optimal configuration: " + this.optimalConfiguration);
        logger.info("Final solution cost after parameter tuning: " + bestSolutionCost);

        return this.optimalConfiguration;

    }


    private <T> double setOptimalValue(ParameterOptimisationTarget optimisationTarget, Consumer<T> parameterSetter,
                                       List<T> parameterValues) {
        if (parameterValues.size() == 1) {
            parameterSetter.accept(parameterValues.get(0));
            return 0;
        }

        double bestSolutionCost = -1;
        T bestParameterValue = null;

        for (T parameterValue : parameterValues) {
            parameterSetter.accept(parameterValue);

            double currentSolutionCost = optimisationTarget.getSolutionCost(this.optimalConfiguration);
            if (bestParameterValue == null || bestSolutionCost > currentSolutionCost) {
                bestSolutionCost = currentSolutionCost;
                bestParameterValue = parameterValue;
            }
        }

        parameterSetter.accept(bestParameterValue);

        return bestSolutionCost;
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
