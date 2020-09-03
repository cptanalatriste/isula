package isula.aco.simpletsp;

import isula.aco.ConfigurationProvider;
import isula.aco.algorithms.antsystem.RandomNodeSelection;
import isula.aco.algorithms.antsystem.StartPheromoneMatrix;
import org.junit.Test;

import java.util.Map;

import static isula.aco.simpletsp.SimpleTSPEnvironment.SAMPLE_PROBLEM;
import static junit.framework.Assert.assertEquals;

public class RandomNodeSelectionTest {

    @Test
    public void getComponentsWithProbabilities() {

        SimpleTSPAnt ant = new SimpleTSPAnt();

        RandomNodeSelection<Integer, SimpleTSPEnvironment> antPolicy = new RandomNodeSelection<>();
        antPolicy.setAnt(ant);

        SimpleTSPEnvironment environment = new SimpleTSPEnvironment();
        environment.setProblemRepresentation(SAMPLE_PROBLEM);

        ConfigurationProvider configuration = new SimpleTSPConfiguration();

        StartPheromoneMatrix<Integer, SimpleTSPEnvironment> daemonAction = new StartPheromoneMatrix<>();
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