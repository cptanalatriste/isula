package isula.aco.algorithms.acs;

import isula.aco.Ant;
import isula.aco.Environment;
import isula.aco.exception.InvalidInputException;
import isula.aco.test.BaseTestForIsula;
import isula.aco.test.DummyFactory;
import org.junit.Test;

import javax.naming.ConfigurationException;

import static junit.framework.Assert.assertEquals;

public class LocalPheromoneUpdateForAcsTest extends BaseTestForIsula {


    public LocalPheromoneUpdateForAcsTest() throws InvalidInputException, ConfigurationException {
        super(DummyFactory.createDummyAcsConfigurationProvider());
    }

    @Test
    public void testApplyPolicy() {

        getAntColony().addAntPolicies(new LocalPheromoneUpdateForAcs<>());

        Ant<Integer, Environment> ant = getAntColony().getHive().get(0);
        AcsConfigurationProvider configurationProvider = (AcsConfigurationProvider) getConfigurationProvider();
        Environment environment = getEnvironment();


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
