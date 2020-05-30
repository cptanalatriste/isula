package isula.aco.algorithms.acs;

import isula.aco.*;

import java.util.List;

import static isula.aco.algorithms.PheromoneUtils.updatePheromoneForAntSolution;

public class PheromoneDepositForAcs<C, E extends Environment>
        extends DaemonAction<C, E> {

    public PheromoneDepositForAcs() {
        super(DaemonActionType.AFTER_ITERATION_CONSTRUCTION);
    }

    @Override
    public void applyDaemonAction(ConfigurationProvider configurationProvider) {
        Ant<C, E> bestAnt = getAntColony().getBestPerformingAnt(getEnvironment());
        List<C> bestSolution = bestAnt.getSolution();

        updatePheromoneForAntSolution(bestAnt, getEnvironment(), (componentIndex) -> {
            C solutionComponent = bestSolution.get(componentIndex);
            return getNewPheromoneValue(bestAnt, componentIndex,
                    solutionComponent, configurationProvider);
        });
    }

    private Double getNewPheromoneValue(Ant<C, E> ant, Integer componentIndex, C solutionComponent,
                                        ConfigurationProvider configurationProvider) {

        double evaporationRatio = configurationProvider.getEvaporationRatio();
        E environment = getEnvironment();
        Double pheromoneTrailValue = ant.getPheromoneTrailValue(solutionComponent, componentIndex, environment);

        Double pheromoneDecay = (1 - evaporationRatio) * pheromoneTrailValue;
        Double contribution = evaporationRatio * (1 / ant.getSolutionCost(environment));
        return pheromoneDecay + contribution;
    }
}
