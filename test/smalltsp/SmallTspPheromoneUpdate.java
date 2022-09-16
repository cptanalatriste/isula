package smalltsp;

import isula.aco.ConfigurationProvider;
import isula.aco.algorithms.antsystem.OnlinePheromoneUpdate;

public class SmallTspPheromoneUpdate extends OnlinePheromoneUpdate<Integer, SmallTspEnvironment> {

    @Override
    protected double getNewPheromoneValue(Integer solutionComponent, Integer positionInSolution,
                                          SmallTspEnvironment environment,
                                          ConfigurationProvider configurationProvider) {

        Double currentValue = getAnt().getPheromoneTrailValue(solutionComponent, positionInSolution, environment);
        Double contribution = 1.0 / getAnt().getSolutionCost(environment);
        return currentValue + contribution;

    }
}
