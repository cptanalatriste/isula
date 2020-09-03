package isula.aco.simpletsp;

import isula.aco.AcoProblemSolver;
import isula.aco.Ant;
import isula.aco.algorithms.antsystem.PerformEvaporation;
import isula.aco.algorithms.antsystem.StartPheromoneMatrix;
import org.junit.Test;

import javax.naming.ConfigurationException;
import java.util.List;

import static isula.aco.simpletsp.SimpleTSPEnvironment.SAMPLE_PROBLEM;
import static org.junit.Assert.assertEquals;

public class PheromoneUpdateTest {

    @Test
    public void addDaemonActions() throws ConfigurationException {

        AcoProblemSolver<Integer, SimpleTSPEnvironment> solver = new AcoProblemSolver<>();
        SimpleTSPEnvironment environment = new SimpleTSPEnvironment();
        environment.setProblemRepresentation(SAMPLE_PROBLEM);

        SimpleTSPConfiguration configurationProvider = new SimpleTSPConfiguration();

        SimpleTSPColony colony = new SimpleTSPColony(configurationProvider.getNumberOfAnts());
        colony.buildColony(environment);


        solver.initialize(environment, colony, configurationProvider);
        StartPheromoneMatrix<Integer, SimpleTSPEnvironment> startPheromoneMatrix = new StartPheromoneMatrix<>();
        startPheromoneMatrix.setEnvironment(environment);
        startPheromoneMatrix.applyDaemonAction(configurationProvider);


        SimpleTSPPheromoneUpdate pheromoneUpdate = new SimpleTSPPheromoneUpdate();
        colony.addAntPolicies(pheromoneUpdate);

        double[][] pheromoneMatrix = environment.getPheromoneMatrix();
        double delta = 0.001;

        PerformEvaporation<Integer, SimpleTSPEnvironment> daemonAction = new PerformEvaporation<>();
        daemonAction.setEnvironment(environment);
        daemonAction.applyDaemonAction(configurationProvider);

        Ant<Integer, SimpleTSPEnvironment> firstAnt = colony.getHive().get(0);
        firstAnt.setSolution(List.of(0, 3, 2, 4, 1));
        firstAnt.doAfterSolutionIsReady(environment, configurationProvider);

        Ant<Integer, SimpleTSPEnvironment> secondAnt = colony.getHive().get(1);
        secondAnt.setSolution(List.of(0, 3, 1, 4, 2));
        secondAnt.doAfterSolutionIsReady(environment, configurationProvider);

        Ant<Integer, SimpleTSPEnvironment> thirdAnt = colony.getHive().get(2);
        thirdAnt.setSolution(List.of(0, 3, 4, 1, 2));
        thirdAnt.doAfterSolutionIsReady(environment, configurationProvider);

        assertEquals(0.5526, pheromoneMatrix[0][3], delta);
        assertEquals(0.5167, pheromoneMatrix[3][4], delta);
        assertEquals(0.5359, pheromoneMatrix[4][1], delta);
        assertEquals(0.5167, pheromoneMatrix[1][2], delta);


    }
}