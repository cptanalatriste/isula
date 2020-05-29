package isula.aco.test;

import isula.aco.Ant;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;
import isula.aco.algorithms.acs.AcsConfigurationProvider;
import isula.aco.algorithms.antsystem.AntSystemConfigurationProvider;
import isula.aco.exception.InvalidInputException;

import java.util.List;

public class DummyFactory {

    private DummyFactory() {

    }

    /**
     * We're creating a dummy Environment instance, with a pheromone matrix with
     * the dimensions specified in the parameters.
     */
    public static Environment createDummyEnvironment(double[][] problemGraph,
                                                     final int pheromoneRows, final int pheromoneColumns)
            throws InvalidInputException {
        return new Environment(problemGraph) {

            @Override
            protected double[][] createPheromoneMatrix() {
                return new double[pheromoneRows][pheromoneColumns];
            }

        };
    }

    /**
     * Creates a configuration provider for testing purposes.
     */
    public static ConfigurationProvider createDummyConfigurationProvider() {
        return new AntSystemConfigurationProvider() {

            @Override
            public double getPheromoneDepositFactor() {
                return 1.0;
            }

            public int getNumberOfIterations() {
                return 1;
            }

            public int getNumberOfAnts() {
                return 1;
            }

            public double getInitialPheromoneValue() {
                return 1.0;
            }

            @Override
            public double getHeuristicImportance() {
                return 1;
            }

            @Override
            public double getPheromoneImportance() {
                return 1;
            }

            public double getEvaporationRatio() {
                return 0.1;
            }
        };
    }

    public static AcsConfigurationProvider createDummyAcsConfigurationProvider() {
        return new AcsConfigurationProvider() {

            @Override
            public int getNumberOfAnts() {
                return 1;
            }

            @Override
            public double getEvaporationRatio() {
                return 0.1;
            }

            @Override
            public int getNumberOfIterations() {
                return 1;
            }

            @Override
            public double getInitialPheromoneValue() {
                return 1.0;
            }

            @Override
            public double getHeuristicImportance() {
                return 1;
            }

            @Override
            public double getPheromoneImportance() {
                return 1;
            }

            @Override
            public double getBestChoiceProbability() {
                return 0.5;
            }

            @Override
            public double getPheromoneDecayCoefficient() {
                return 0.1;
            }
        };
    }

    /**
     * Configures a Dummy Ant for testing.
     */
    public static Ant<Integer, Environment> createDummyAnt(
            final int expectedCost, final int indexLimit) {
        Ant<Integer, Environment> ant = new Ant<Integer, Environment>() {

            @Override
            public List<Integer> getNeighbourhood(Environment environment) {
                return null;
            }

            @Override
            public Double getPheromoneTrailValue(Integer solutionComponent,
                                                 Integer positionInSolution, Environment environment) {
                return environment.getPheromoneMatrix()[solutionComponent][positionInSolution];
            }

            @Override
            public Double getHeuristicValue(Integer solutionComponent,
                                            Integer positionInSolution, Environment environment) {
                return null;
            }

            @Override
            public void setPheromoneTrailValue(Integer solutionComponent,
                                               Integer positionInSolution, Environment environment, Double value) {

                double[][] pheromoneMatrix = environment.getPheromoneMatrix();
                pheromoneMatrix[solutionComponent][positionInSolution] = value;

            }

            @Override
            public double getSolutionCost(Environment environment) {
                return expectedCost;
            }

            @Override
            public boolean isSolutionReady(Environment environment) {
                return this.getCurrentIndex() == indexLimit;
            }

        };

        ant.setSolution(new Integer[indexLimit]);
        return ant;
    }

}
