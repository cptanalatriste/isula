package isula.aco.algorithms.antsystem;

import isula.aco.ConfigurationProvider;
import org.junit.Test;
import smalltsp.SmallTspAnt;
import smalltsp.SmallTspConfiguration;
import smalltsp.SmallTspEnvironment;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static smalltsp.SmallTspEnvironment.SAMPLE_PROBLEM;

public class RandomNodeSelectionTest {

    @Test
    public void getComponentsWithProbabilities() {

        SmallTspAnt ant = new SmallTspAnt();

        RandomNodeSelection<Integer, SmallTspEnvironment> antPolicy = new RandomNodeSelection<>();
        antPolicy.setAnt(ant);

        SmallTspEnvironment environment = new SmallTspEnvironment();
        environment.setProblemRepresentation(SAMPLE_PROBLEM);

        ConfigurationProvider configuration = new SmallTspConfiguration();

        StartPheromoneMatrix<Integer, SmallTspEnvironment> daemonAction = new StartPheromoneMatrix<>();
        daemonAction.setEnvironment(environment);
        daemonAction.applyDaemonAction(configuration);

        ant.visitNode(0, environment);
        Map<Integer, Double> componentWithProbabilities = antPolicy.getComponentsWithProbabilities(
                environment, configuration);

        double deltaForComparison = 0.001;
        assertEquals(0.3299, componentWithProbabilities.get(1), deltaForComparison);
        assertEquals(0.2291, componentWithProbabilities.get(2), deltaForComparison);
        assertEquals(0.2727, componentWithProbabilities.get(3), deltaForComparison);
        assertEquals(0.1683, componentWithProbabilities.get(4), deltaForComparison);

        ant.visitNode(3, environment);
        componentWithProbabilities = antPolicy.getComponentsWithProbabilities(
                environment, configuration);

        assertEquals(0.2147, componentWithProbabilities.get(1), deltaForComparison);
        assertEquals(0.5965, componentWithProbabilities.get(2), deltaForComparison);
        assertEquals(0.1887, componentWithProbabilities.get(4), deltaForComparison);


    }
}