package isula.aco.simpletsp;

import isula.aco.ConfigurationProvider;
import isula.aco.algorithms.antsystem.OnlinePheromoneUpdate;

public class SimpleTSPPheromoneUpdate extends OnlinePheromoneUpdate<Integer, SimpleTSPEnvironment> {

    @Override
    protected double getNewPheromoneValue(Integer solutionComponent, Integer positionInSolution,
                                          SimpleTSPEnvironment environment,
                                          ConfigurationProvider configurationProvider) {

        Double currentValue = getAnt().getPheromoneTrailValue(solutionComponent, positionInSolution, environment);
        Double contribution = 1.0 / getAnt().getSolutionCost(environment);
        return currentValue + contribution;

    }
}
