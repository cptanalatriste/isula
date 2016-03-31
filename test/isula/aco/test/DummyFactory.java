package isula.aco.test;

import isula.aco.Ant;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;
import isula.aco.exception.InvalidInputException;

import java.util.List;

public class DummyFactory {

    private DummyFactory() {

    }

    /**
     * We're creating a dummy Environment instance, with a pheromone matrix with
     * the dimensions specified in the parameters.
     *
     * @param problemGraph
     * @param pheromoneRows
     * @param pheromoneColumns
     * @return
     * @throws InvalidInputException
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
     *
     * @return
     */
    public static ConfigurationProvider createDummyConfigurationProvider() {
        return new ConfigurationProvider() {

            public int getNumberOfIterations() {
                return 0;
            }

            public int getNumberOfAnts() {
                return 0;
            }

            public double getInitialPheromoneValue() {
                return 0;
            }

            @Override
            public double getHeuristicImportance() {
                return 0;
            }

            @Override
            public double getPheromoneImportance() {
                return 0;
            }

            public double getEvaporationRatio() {
                return 0;
            }
        };
    }

    /**
     * Configures a Dummy Ant for testing.
     *
     * @return
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
                return null;
            }

            @Override
            public Double getHeuristicValue(Integer solutionComponent,
                                            Integer positionInSolution, Environment environment) {
                return null;
            }

            @Override
            public void setPheromoneTrailValue(Integer solutionComponent,
                                               Integer positionInSolution, Environment environment, Double value) {
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
