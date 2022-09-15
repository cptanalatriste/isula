package isula.aco;

import isula.aco.algorithms.antsystem.OfflinePheromoneUpdate;
import isula.aco.algorithms.antsystem.PerformEvaporation;
import isula.aco.exception.ConfigurationException;
import isula.aco.exception.InvalidInputException;
import isula.aco.test.BaseTestForIsula;
import isula.aco.test.DummyFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AcoProblemSolverTest extends BaseTestForIsula {

    private final PerformEvaporation<Integer, Environment> evaporationDaemonAction = new PerformEvaporation<>();
    private final OfflinePheromoneUpdate<Integer, Environment> pheromoneUpdateDaemonAction =
            new OfflinePheromoneUpdate<>();

    public AcoProblemSolverTest() throws InvalidInputException, ConfigurationException {
        super(DummyFactory.createDummyConfigurationProvider());
        getProblemSolver().addDaemonActions(evaporationDaemonAction, pheromoneUpdateDaemonAction);

    }

    @Test
    public void testOptimisationOutcome() {
        getProblemSolver().solveProblem();
        assertEquals(SOLUTION_COST, getProblemSolver().getBestSolutionCost(), 0.001);
        List<String> expectedSolution = new ArrayList<>();
        IntStream.rangeClosed(1, SOLUTION_LENGTH)
                .forEach((item) -> expectedSolution.add(String.valueOf(COMPONENT_FROM_POLICY)));

        String bestSolutionAsString = String.join(" ", expectedSolution);
        assertEquals(bestSolutionAsString, getProblemSolver().getBestSolutionAsString().trim());

        String problemSolverAsString = getProblemSolver().toString();
        assertTrue(problemSolverAsString.contains("bestSolution=" + expectedSolution));
        assertTrue(problemSolverAsString.contains("bestSolutionCost=" + SOLUTION_COST));

    }

    @Test
    public void testSolverConfiguration() {
        assertEquals(getEnvironment(), getProblemSolver().getEnvironment());
        assertEquals(getAntColony(), getProblemSolver().getAntColony());

        assertEquals(2, getProblemSolver().getDaemonActions().size());
        assertTrue(getProblemSolver().getDaemonActions().stream().anyMatch(
                action -> action.equals(evaporationDaemonAction)));
        assertTrue(getProblemSolver().getDaemonActions().stream().anyMatch(
                action -> action.equals(pheromoneUpdateDaemonAction)));
    }
}
