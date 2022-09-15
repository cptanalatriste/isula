package isula.aco;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import isula.aco.algorithms.antsystem.OnlinePheromoneUpdate;
import isula.aco.exception.InvalidInputException;
import isula.aco.test.DummyFactory;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AntTest {

    private static final double[][] SAMPLE_PROBLEM_GRAPH = new double[3][3];

    private Ant<Integer, Environment> dummyAnt;
    private OnlinePheromoneUpdate<Integer, Environment> onlinePheromoneUpdatePolicy =
            new OnlinePheromoneUpdate<>() {
                @Override
                protected double getNewPheromoneValue(Integer solutionComponent, Integer positionInSolution,
                                                      Environment environment, ConfigurationProvider configurationProvider) {
                    return 0;
                }
            };

    /**
     * Configures a Dummy Ant for testing.
     */
    @Before
    public void setUp() {
        this.dummyAnt = DummyFactory.createDummyAnt(0, 1, null);

        this.dummyAnt.setSolution(new ArrayList<>());

        this.dummyAnt.visitNode(0, null);
        this.dummyAnt.visitNode(1, null);
        this.dummyAnt.visitNode(2, null);
    }

    @Test
    public void testVisitNode() {

        assertEquals(3, this.dummyAnt.getCurrentIndex());
        assertTrue(this.dummyAnt.isNodeVisited(0));
        assertTrue(this.dummyAnt.isNodeVisited(1));
        assertTrue(this.dummyAnt.isNodeVisited(2));
        assertFalse(this.dummyAnt.isNodeVisited(3));
    }

    @Test
    public void testClear() {
        this.dummyAnt.clear();

        assertEquals(0, this.dummyAnt.getCurrentIndex());
        assertTrue(this.dummyAnt.getVisited().isEmpty());

        List<Integer> solution = this.dummyAnt.getSolution();

        for (Integer component : solution) {
            assertNull(component);
        }
    }

    @Test
    public void testGetSolutionAsString() {
        String expectedString = " 0 1 2";

        assertEquals(expectedString, this.dummyAnt.getSolutionAsString());
    }

    @Test
    public void testAddPolicy() {

        this.dummyAnt.addPolicy(onlinePheromoneUpdatePolicy);

        AntPolicy<Integer, Environment> extractedPolicy = this.dummyAnt.getAntPolicies(
                onlinePheromoneUpdatePolicy.getPolicyType(), 1).get(0);
        assertEquals(onlinePheromoneUpdatePolicy, extractedPolicy);
        assertEquals(0, this.dummyAnt.getAntPolicies(AntPolicyType.NODE_SELECTION, -1).size());
    }

    @Test
    public void testDoAfterSolutionIsReady() throws InvalidInputException {

        Environment dummyEnvironment = DummyFactory.createDummyEnvironment(
                SAMPLE_PROBLEM_GRAPH, 3, 3);

        ConfigurationProvider configurationProvider = DummyFactory
                .createDummyConfigurationProvider();

        this.dummyAnt.addPolicy(onlinePheromoneUpdatePolicy);
        this.dummyAnt.doAfterSolutionIsReady(dummyEnvironment,
                configurationProvider);

        assertEquals(this.dummyAnt, onlinePheromoneUpdatePolicy.getAnt());
    }

}
