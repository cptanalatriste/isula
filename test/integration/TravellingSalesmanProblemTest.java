package integration;

import isula.aco.exception.ConfigurationException;
import org.junit.Before;
import org.junit.Test;
import tsp.TspHelper;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TravellingSalesmanProblemTest {

    private BruteForceTspSolver bruteForceSolver;
    private AntColonyTspSolver acoTspSolver;


    @Before
    public void setUp() {
        bruteForceSolver = new BruteForceTspSolver();
        acoTspSolver = new AntColonyTspSolver();
    }

    @Test
    public void testSolveSmallInstance() throws ConfigurationException {
        Map<String, Map<String, Integer>> distanceMap = TspHelper.getSampleProblem();

        List<String> acoSolution = acoTspSolver.findOptimalRoute(distanceMap);
        List<List<String>> actualSolutions = bruteForceSolver.findOptimalRoutes(distanceMap);

        assertTrue(actualSolutions.stream()
                .anyMatch(route -> route.equals(acoSolution)));

        Integer bestSolutionCost = TspHelper.calculateDistance(actualSolutions.get(0), distanceMap);
        assertEquals(acoTspSolver.getSolver().getBestSolutionCost(), bestSolutionCost, 0.001);

    }
}
