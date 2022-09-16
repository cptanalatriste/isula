package integration;

import isula.aco.AcoProblemSolver;
import isula.aco.algorithms.antsystem.OfflinePheromoneUpdate;
import isula.aco.algorithms.antsystem.PerformEvaporation;
import isula.aco.algorithms.antsystem.RandomNodeSelection;
import isula.aco.algorithms.antsystem.StartPheromoneMatrix;
import isula.aco.exception.ConfigurationException;
import tsp.AntColonyForTsp;
import tsp.TspConfiguration;
import tsp.TspEnvironment;

import java.util.List;
import java.util.Map;

public class AntColonyTspSolver {


    private final AcoProblemSolver<String, TspEnvironment> solver;

    public AntColonyTspSolver() {
        solver = new AcoProblemSolver<>();
    }

    public List<String> findOptimalRoute(Map<String, Map<String, Integer>> distanceMap)
            throws ConfigurationException {

        //The environment contains the distance information and the pheromone values.
        TspEnvironment environment = new TspEnvironment(distanceMap);
        //This object contains the algorithm parameters (number of ants, initial pheromone, etc.).
        TspConfiguration configuration = new TspConfiguration(environment);
        //The colony administers the ants. It creates them and makes them build solutions.
        AntColonyForTsp colony = new AntColonyForTsp(configuration.getNumberOfAnts());

        //The solver orchestrates the whole process.
        solver.initialize(environment, colony, configuration);

        //Daemon actions are external events, not associated with the ants. We are adding pheromone
        // initialization, evaporation, and updating pheromones at the end of solution building.
        solver.addDaemonActions(new StartPheromoneMatrix<>(), new PerformEvaporation<>());
        solver.addDaemonActions(new OfflinePheromoneUpdate<>());
        //This ant policy contains the rules for adding new components to the ant's solution.
        solver.getAntColony().addAntPolicies(new RandomNodeSelection<>());

        solver.solveProblem();
        return solver.getBestSolution();
    }

    public AcoProblemSolver<String, TspEnvironment> getSolver() {
        return solver;
    }


}
