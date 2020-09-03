package isula.aco.simpletsp;

import isula.aco.ConfigurationProvider;

public class SimpleTSPConfiguration implements ConfigurationProvider {

    @Override
    public int getNumberOfAnts() {
        return 3;
    }

    @Override
    public double getEvaporationRatio() {
        return 0.5;
    }

    @Override
    public int getNumberOfIterations() {
        return 0;
    }

    @Override
    public double getInitialPheromoneValue() {
        return 1.0;
    }

    @Override
    public double getHeuristicImportance() {
        return 2;
    }

    @Override
    public double getPheromoneImportance() {
        return 1;
    }
}
