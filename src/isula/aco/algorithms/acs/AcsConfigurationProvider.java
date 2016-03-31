package isula.aco.algorithms.acs;

import isula.aco.ConfigurationProvider;

/**
 * A configuration provider, but for Ant Colony System algorithm.
 *
 * @author Carlos G. Gavidia
 */
public interface AcsConfigurationProvider extends ConfigurationProvider {

    double getBestChoiceProbability();
}
