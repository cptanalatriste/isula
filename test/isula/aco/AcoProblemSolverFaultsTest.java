package isula.aco;

import isula.aco.algorithms.antsystem.AntSystemConfigurationProvider;
import isula.aco.exception.ConfigurationException;
import isula.aco.exception.InvalidInputException;
import isula.aco.test.BaseTestForIsula;
import org.junit.Test;

import java.time.Duration;

public class AcoProblemSolverFaultsTest extends BaseTestForIsula {


    public AcoProblemSolverFaultsTest() throws InvalidInputException {
        super(createFaultyConfigurationProvider());
    }

    @Test(expected = ConfigurationException.class)
    public void testColonyNotPresent() throws ConfigurationException {
        AcoProblemSolver<Integer, Environment> solver = new AcoProblemSolver<>();
        solver.initialize(getEnvironment(), null, getConfigurationProvider(), Duration.ofSeconds(60));
    }

    @Test(expected = ConfigurationException.class)
    public void testNoIterations() {
        getProblemSolver().solveProblem();
    }

    @Test(expected = ConfigurationException.class)
    public void testNoConfigurationProvider() {
        AcoProblemSolver<Integer, Environment> solver = new AcoProblemSolver<>();
        solver.initialize(getEnvironment(), getAntColony(), null, Duration.ofSeconds(60));

        solver.getConfigurationProvider();
    }

    private static ConfigurationProvider createFaultyConfigurationProvider() {
        return new AntSystemConfigurationProvider() {

            @Override
            public double getPheromoneDepositFactor() {
                return 1.0;
            }

            public int getNumberOfIterations() {
                return 0;
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
}
