package isula.aco.algorithms.acs;

import isula.aco.*;
import isula.aco.algorithms.antsystem.StartPheromoneMatrix;
import isula.aco.exception.InvalidInputException;
import isula.aco.test.DummyFactory;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class LocalPheromoneUpdateForAcsTest {

    private static final int SOLUTION_LENGTH = 3;
    private static final int COMMON_COST = 2;
    protected static final Integer COMPONENT_FROM_POLICY = 0;

    private Ant<Integer, Environment> ant;
    private AcsConfigurationProvider configurationProvider;
    private Environment environment;

    @Before
    public void setUp() throws InvalidInputException {
        AntPolicy<Integer, Environment> nodeSelectionPolicy = new AntPolicy<Integer, Environment>(
                AntPolicyType.NODE_SELECTION) {

            @Override
            public boolean applyPolicy(Environment environment,
                                       ConfigurationProvider configurationProvider) {
                this.getAnt().visitNode(COMPONENT_FROM_POLICY);
                return true;
            }
        };

        ant = DummyFactory.createDummyAnt(COMMON_COST, SOLUTION_LENGTH);
        ant.addPolicy(nodeSelectionPolicy);
        ant.addPolicy(new LocalPheromoneUpdateForAcs<>());

        double[][] problemGraph = new double[3][4];
        int pheromoneRows = 3;
        int pheromoneColumns = 4;
        environment = DummyFactory.createDummyEnvironment(problemGraph,
                pheromoneRows, pheromoneColumns);
        configurationProvider = DummyFactory.createDummyAcsConfigurationProvider();
        StartPheromoneMatrix<Integer, Environment> startPheromoneMatrix = new StartPheromoneMatrix<>();
        startPheromoneMatrix.setEnvironment(environment);
        startPheromoneMatrix.applyDaemonAction(configurationProvider);
    }

    @Test
    public void testApplyPolicy(){



        double t_0 = configurationProvider.getInitialPheromoneValue();
        double epsilon = configurationProvider.getPheromoneDecayCoefficient();

        double t_ij = ant.getPheromoneTrailValue(COMPONENT_FROM_POLICY, 0, environment);
        ant.selectNextNode(environment, configurationProvider);
        double expectedPheromoneValue = (1 - epsilon) * t_ij + t_0 * epsilon;
        double pheromoneAfterVisit = ant.getPheromoneTrailValue(COMPONENT_FROM_POLICY, 0, environment);
        assertEquals(pheromoneAfterVisit, expectedPheromoneValue);


        t_ij = ant.getPheromoneTrailValue(COMPONENT_FROM_POLICY, 1, environment);
        ant.selectNextNode(environment, configurationProvider);
        pheromoneAfterVisit = ant.getPheromoneTrailValue(COMPONENT_FROM_POLICY, 1, environment);
        expectedPheromoneValue = (1 - epsilon) * t_ij + t_0 * epsilon;
        assertEquals(pheromoneAfterVisit, expectedPheromoneValue);

    }
}
