package isula.aco.tuning;

import isula.aco.ConfigurationProvider;

public interface ParameterOptimisationTarget {

    double getSolutionCost(ConfigurationProvider configurationProvider);
}
