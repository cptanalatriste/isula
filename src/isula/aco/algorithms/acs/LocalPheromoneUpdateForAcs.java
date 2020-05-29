package isula.aco.algorithms.acs;

import isula.aco.AntPolicy;
import isula.aco.AntPolicyType;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;

import static isula.aco.algorithms.PheromoneUtils.validatePheromoneValue;

public class LocalPheromoneUpdateForAcs<C, E extends Environment> extends
        AntPolicy<C, E> {

    public LocalPheromoneUpdateForAcs() {
        super(AntPolicyType.AFTER_NODE_SELECTION);
    }

    @Override
    public boolean applyPolicy(E environment, ConfigurationProvider configurationProvider) {

        int lastVisitedPosition = getAnt().getCurrentIndex() - 1;
        C solutionComponent = getAnt().getSolution()[lastVisitedPosition];

        double newPheromoneValue = this.getNewPheromoneValue(solutionComponent, lastVisitedPosition,
                environment, configurationProvider);
        validatePheromoneValue(newPheromoneValue);

        getAnt().setPheromoneTrailValue(solutionComponent, lastVisitedPosition, environment,
                newPheromoneValue);
        return true;
    }

    private double getNewPheromoneValue(C solutionComponent, int lastVisitedPosition, E environment,
                                        ConfigurationProvider configurationProvider) {

        AcsConfigurationProvider configuration = (AcsConfigurationProvider) configurationProvider;
        Double afterEvaporation = (1 - configuration.getPheromoneDecayCoefficient()) * getAnt().getPheromoneTrailValue(
                solutionComponent, lastVisitedPosition, environment);
        Double contribution = configuration.getPheromoneDecayCoefficient() * configurationProvider.getInitialPheromoneValue();

        return afterEvaporation + contribution;
    }
}
