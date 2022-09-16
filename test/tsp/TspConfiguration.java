package tsp;

import isula.aco.algorithms.antsystem.AntSystemConfigurationProvider;

public class TspConfiguration implements AntSystemConfigurationProvider {

    private final TspEnvironment environment;

    public TspConfiguration(TspEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public int getNumberOfAnts() {
        return this.environment.getAllCities().size() / 2;
    }

    @Override
    public double getEvaporationRatio() {
        return 0.4;
    }

    @Override
    public int getNumberOfIterations() {
        return 1000;
    }

    @Override
    public double getInitialPheromoneValue() {
        return 1;
    }

    @Override
    public double getHeuristicImportance() {
        return 7;
    }

    @Override
    public double getPheromoneImportance() {
        return 4;
    }

    @Override
    public double getPheromoneDepositFactor() {
        return 1;
    }
}
