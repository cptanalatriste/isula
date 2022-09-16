package isula.aco.algorithms.antsystem;

import isula.aco.AcoProblemSolver;
import isula.aco.Ant;
import org.junit.Test;
import smalltsp.SmallTspAntColony;
import smalltsp.SmallTspConfiguration;
import smalltsp.SmallTspEnvironment;
import smalltsp.SmallTspPheromoneUpdate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static smalltsp.SmallTspEnvironment.SAMPLE_PROBLEM;

public class PheromoneUpdateTest {

    @Test
    public void addDaemonActions() {

        AcoProblemSolver<Integer, SmallTspEnvironment> solver = new AcoProblemSolver<>();
        SmallTspEnvironment environment = new SmallTspEnvironment();
        environment.setProblemRepresentation(SAMPLE_PROBLEM);

        SmallTspConfiguration configurationProvider = new SmallTspConfiguration();

        SmallTspAntColony colony = new SmallTspAntColony(configurationProvider.getNumberOfAnts());
        colony.buildColony(environment);


        solver.initialize(environment, colony, configurationProvider);
        StartPheromoneMatrix<Integer, SmallTspEnvironment> startPheromoneMatrix = new StartPheromoneMatrix<>();
        startPheromoneMatrix.setEnvironment(environment);
        startPheromoneMatrix.applyDaemonAction(configurationProvider);


        SmallTspPheromoneUpdate pheromoneUpdate = new SmallTspPheromoneUpdate();
        colony.addAntPolicies(pheromoneUpdate);

        double[][] pheromoneMatrix = environment.getPheromoneMatrix();
        double delta = 0.001;

        PerformEvaporation<Integer, SmallTspEnvironment> daemonAction = new PerformEvaporation<>();
        daemonAction.setEnvironment(environment);
        daemonAction.applyDaemonAction(configurationProvider);

        Ant<Integer, SmallTspEnvironment> firstAnt = colony.getHive().get(0);
        firstAnt.setSolution(List.of(0, 3, 2, 4, 1));
        firstAnt.doAfterSolutionIsReady(environment, configurationProvider);

        Ant<Integer, SmallTspEnvironment> secondAnt = colony.getHive().get(1);
        secondAnt.setSolution(List.of(0, 3, 1, 4, 2));
        secondAnt.doAfterSolutionIsReady(environment, configurationProvider);

        Ant<Integer, SmallTspEnvironment> thirdAnt = colony.getHive().get(2);
        thirdAnt.setSolution(List.of(0, 3, 4, 1, 2));
        thirdAnt.doAfterSolutionIsReady(environment, configurationProvider);

        assertEquals(0.5526, pheromoneMatrix[0][3], delta);
        assertEquals(0.5167, pheromoneMatrix[3][4], delta);
        assertEquals(0.5359, pheromoneMatrix[4][1], delta);
        assertEquals(0.5167, pheromoneMatrix[1][2], delta);


    }
}