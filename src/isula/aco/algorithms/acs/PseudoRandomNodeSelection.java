package isula.aco.algorithms.acs;

import isula.aco.AntPolicy;
import isula.aco.AntPolicyType;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

public class PseudoRandomNodeSelection<E> extends AntPolicy<E> {

  public PseudoRandomNodeSelection() {
    super(AntPolicyType.NODE_SELECTION);
  }

  @Override
  public void applyPolicy(Environment environment,
      ConfigurationProvider configuration) {
    E nextNode = null;
    Random random = new Random();
    double randomValue = random.nextDouble();

    AcsConfigurationProvider configurationProvider = (AcsConfigurationProvider) configuration;

    double bestChoiceProbability = configurationProvider
        .getBestChoiceProbability();

    HashMap<E, Double> componentsWithProbabilities = this
        .getComponentsWithProbabilities(environment, configurationProvider);

    if (randomValue < bestChoiceProbability) {
      // TODO(cgavidia): This branch has testing pending.
      double currentMaximumProbability = -1;

      Iterator<Entry<E, Double>> componentWithProbabilitiesIterator = componentsWithProbabilities
          .entrySet().iterator();
      while (componentWithProbabilitiesIterator.hasNext()) {
        Entry<E, Double> componentWithProbability = componentWithProbabilitiesIterator
            .next();

        E possibleMove = componentWithProbability.getKey();
        double currentProbability = componentWithProbability.getValue();

        if (!getAnt().isNodeVisited(possibleMove)
            && currentProbability > currentMaximumProbability) {

          nextNode = possibleMove;
          currentMaximumProbability = currentProbability;
        }
      }

      getAnt().visitNode(nextNode);
    } else {

      double value = random.nextDouble();
      double total = 0;

      Iterator<Entry<E, Double>> componentWithProbabilitiesIterator = componentsWithProbabilities
          .entrySet().iterator();
      while (componentWithProbabilitiesIterator.hasNext()) {
        Entry<E, Double> componentWithProbability = componentWithProbabilitiesIterator
            .next();

        total += componentWithProbability.getValue();

        if (total >= value) {
          nextNode = componentWithProbability.getKey();
          getAnt().visitNode(nextNode);
          return;
        }
      }
    }
  }

  /**
   * Gets a probabilities vector, containing probabilities to move to each node
   * according to pheromone matrix.
   * 
   * @param environment
   *          Environment that ants are traversing.
   * @param configurationProvider
   *          Configuration provider.
   * @return Probabilities for the adjacent nodes.
   */
  private HashMap<E, Double> getComponentsWithProbabilities(
      Environment environment, AcsConfigurationProvider configurationProvider) {
    HashMap<E, Double> componentsWithProbabilities = new HashMap<E, Double>();

    double denominator = 0.0;
    for (E possibleMove : getAnt().getNeighbourhood(environment)) {
      if (!getAnt().isNodeVisited(possibleMove)) {

        Double heuristicTimesPheromone = getHeuristicTimesPheromone(
            environment, configurationProvider, possibleMove);

        denominator += heuristicTimesPheromone;
        componentsWithProbabilities.put(possibleMove, 0.0);
      }
    }

    Iterator<Entry<E, Double>> componentWithProbabilitiesIterator = componentsWithProbabilities
        .entrySet().iterator();
    while (componentWithProbabilitiesIterator.hasNext()) {
      Entry<E, Double> componentWithProbability = componentWithProbabilitiesIterator
          .next();
      E component = componentWithProbability.getKey();

      Double numerator = getHeuristicTimesPheromone(environment,
          configurationProvider, component);
      componentWithProbability.setValue(numerator / denominator);
    }

    return componentsWithProbabilities;
  }

  private Double getHeuristicTimesPheromone(Environment environment,
      AcsConfigurationProvider configurationProvider, E possibleMove) {
    Double heuristicValue = getAnt().getHeuristicValue(possibleMove,
        getAnt().getCurrentIndex(), environment);
    Double pheromoneTrailValue = getAnt().getPheromoneTrailValue(possibleMove,
        getAnt().getCurrentIndex(), environment);

    Double heuristicTimesPheromone = Math.pow(heuristicValue,
        configurationProvider.getHeuristicImportance())
        * Math.pow(pheromoneTrailValue,
            configurationProvider.getPheromoneImportance());
    return heuristicTimesPheromone;
  }
}
