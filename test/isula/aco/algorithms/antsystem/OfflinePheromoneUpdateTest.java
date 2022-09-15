package isula.aco.algorithms.antsystem;

import isula.aco.Ant;
import isula.aco.Environment;
import isula.aco.exception.ConfigurationException;
import isula.aco.exception.InvalidInputException;
import isula.aco.test.BaseTestForIsula;
import isula.aco.test.DummyFactory;
import org.junit.Test;


import java.util.List;

import static org.junit.Assert.assertEquals;

public class OfflinePheromoneUpdateTest extends BaseTestForIsula {

    public OfflinePheromoneUpdateTest() throws InvalidInputException, ConfigurationException {
        super(DummyFactory.createDummyConfigurationProvider());
    }

    @Test
    public void applyDaemonAction() throws ConfigurationException {

        getProblemSolver().addDaemonActions(new PerformEvaporation<>(), new OfflinePheromoneUpdate<>());
        getProblemSolver().solveProblem();

        List<Integer> solution = getProblemSolver().getBestSolution();
        Ant<Integer, Environment> ant = getAntColony().getBestPerformingAnt(getEnvironment());

        AntSystemConfigurationProvider configurationProvider = (AntSystemConfigurationProvider) this.getConfigurationProvider();
        double t_ij = configurationProvider.getInitialPheromoneValue();
        double rho = 1 - configurationProvider.getEvaporationRatio();
        double Q = configurationProvider.getPheromoneDepositFactor();
        double F = SOLUTION_COST;

        double expectedPheromone = (1 - rho) * t_ij + Q / F;

        for (int componentIndex = 0; componentIndex < solution.size(); componentIndex++) {
            double pheromoneValue = ant.getPheromoneTrailValue(solution.get(componentIndex), componentIndex,
                    getEnvironment());
            assertEquals(expectedPheromone, pheromoneValue, 0.001);
        }
    }
}