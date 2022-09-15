package isula.aco;

import isula.aco.exception.InvalidInputException;
import isula.aco.test.BaseTestForIsula;
import isula.aco.test.DummyFactory;
import org.junit.Test;

import javax.naming.ConfigurationException;
import java.time.Instant;

public class PerformanceTrackerTest extends BaseTestForIsula {

    public PerformanceTrackerTest() throws InvalidInputException, ConfigurationException {
        super(DummyFactory.createDummyConfigurationProvider());
    }

    @Test(expected = Exception.class)
    public void testUpdateIterationPerformance() throws ConfigurationException {

        PerformanceTracker<Integer, Environment> performanceTracker = getProblemSolver().kickOffColony(getAntColony(),
                this.getEnvironment(), Instant.now());

        performanceTracker.getBestSolution().add(-1);
        performanceTracker.getBestSolution().clear();
    }
}