package isula.aco.tuning;

import isula.aco.ConfigurationProvider;

import java.util.Objects;

public class BasicConfigurationProvider implements ConfigurationProvider {

    private int numberOfAnts;
    private double evaporationRatio;
    private int numberOfIterations;
    private double initialPheromoneValue;
    private double heuristicImportance;
    private double pheromoneImportance;

    @Override
    public int getNumberOfAnts() {
        return numberOfAnts;
    }

    @Override
    public double getEvaporationRatio() {
        return evaporationRatio;
    }

    @Override
    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    @Override
    public double getInitialPheromoneValue() {
        return initialPheromoneValue;
    }

    @Override
    public double getHeuristicImportance() {
        return heuristicImportance;
    }

    @Override
    public double getPheromoneImportance() {
        return pheromoneImportance;
    }


    public void setNumberOfAnts(int numberOfAnts) {
        this.numberOfAnts = numberOfAnts;
    }

    public void setEvaporationRatio(double evaporationRatio) {
        this.evaporationRatio = evaporationRatio;
    }

    public void setNumberOfIterations(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public void setInitialPheromoneValue(double initialPheromoneValue) {
        this.initialPheromoneValue = initialPheromoneValue;
    }

    public void setHeuristicImportance(double heuristicImportance) {
        this.heuristicImportance = heuristicImportance;
    }

    public void setPheromoneImportance(double pheromoneImportance) {
        this.pheromoneImportance = pheromoneImportance;
    }

    @Override
    public String toString() {
        return "BasicConfigurationProvider{" +
                "numberOfAnts=" + numberOfAnts +
                ", evaporationRatio=" + evaporationRatio +
                ", numberOfIterations=" + numberOfIterations +
                ", initialPheromoneValue=" + initialPheromoneValue +
                ", heuristicImportance=" + heuristicImportance +
                ", pheromoneImportance=" + pheromoneImportance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicConfigurationProvider that = (BasicConfigurationProvider) o;
        return numberOfAnts == that.numberOfAnts &&
                Double.compare(that.evaporationRatio, evaporationRatio) == 0 &&
                numberOfIterations == that.numberOfIterations &&
                Double.compare(that.initialPheromoneValue, initialPheromoneValue) == 0 &&
                Double.compare(that.heuristicImportance, heuristicImportance) == 0 &&
                Double.compare(that.pheromoneImportance, pheromoneImportance) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfAnts, evaporationRatio, numberOfIterations, initialPheromoneValue, heuristicImportance, pheromoneImportance);
    }
}
