package isula.aco.algorithms.acs;

import isula.aco.AntPolicy;
import isula.aco.AntPolicyType;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

//TODO(cgavidia): Temporarly, using Integer for this policy. However, it should su
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

    double[][] problemGraph = environment.getProblemGraph();

    // TODO(cgavidia): This should come from configuration.
    double bestChoiceProbability = (problemGraph.length - configurationProvider
        .getBestChoiceConstant()) / problemGraph.length;

    if (randomValue < bestChoiceProbability) {
      // TODO(cgavidia): This branch has testing pending.
      double currentMaximumFeromone = -1;

      for (E possibleMove : getAnt().getNeighbourhood(environment)) {
        double currentPheromone = getAnt().getPheromoneTrailValue(possibleMove,
            getAnt().getCurrentIndex(), environment);
        if (!getAnt().isNodeVisited(possibleMove)
            && currentPheromone > currentMaximumFeromone) {

          nextNode = possibleMove;
          currentMaximumFeromone = currentPheromone;
        }
      }

      getAnt().visitNode(nextNode);
    } else {
      HashMap<E, Double> componentsWithProbabilities = this
          .getComponentsWithProbabilities(environment);
      double value = randomValue;
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
   * @return Probabilities for the adjacent nodes.
   */
  private HashMap<E, Double> getComponentsWithProbabilities(
      Environment environment) {
    HashMap<E, Double> componentsWithProbabilities = new HashMap<E, Double>();

    double denominator = 0.0;
    for (E possibleMove : getAnt().getNeighbourhood(environment)) {
      if (!getAnt().isNodeVisited(possibleMove)) {
        denominator += getAnt().getPheromoneTrailValue(possibleMove,
            getAnt().getCurrentIndex(), environment);

        componentsWithProbabilities.put(possibleMove, 0.0);
      }
    }

    Iterator<Entry<E, Double>> componentWithProbabilitiesIterator = componentsWithProbabilities
        .entrySet().iterator();
    while (componentWithProbabilitiesIterator.hasNext()) {
      Entry<E, Double> componentWithProbability = componentWithProbabilitiesIterator
          .next();
      E component = componentWithProbability.getKey();

      Double numerator = getAnt().getPheromoneTrailValue(component,
          getAnt().getCurrentIndex(), environment);
      componentWithProbability.setValue(numerator / denominator);
    }

    return componentsWithProbabilities;
  }
}
