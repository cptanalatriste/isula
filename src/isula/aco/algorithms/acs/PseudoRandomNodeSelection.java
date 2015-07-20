package isula.aco.algorithms.acs;

import isula.aco.AntPolicy;
import isula.aco.AntPolicyType;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;
import isula.aco.exception.SolutionConstructionException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

/**
 * The selection policy for Ant Colony System algorithms. It defines to
 * selection rules.
 * 
 * <ul>
 * <li>The first is the best selection rule. It picks the component with the
 * better heuristic and pheromone information.
 * <li>The other one is the usual ACO policy, selecting based on the
 * probabilities of each possible component.
 * <li>
 * </ul>
 *
 * <p>
 * Using one or another is determined by a "best-choice" probability.
 * 
 * @author Carlos G. Gavidia
 *
 * @param <C>
 *          Class for components of a solution.
 * @param <E>
 *          Class representing the Environment.
 */
public class PseudoRandomNodeSelection<C, E extends Environment> extends
    AntPolicy<C, E> {

  public PseudoRandomNodeSelection() {
    super(AntPolicyType.NODE_SELECTION);
  }

  @Override
  public void applyPolicy(E environment, ConfigurationProvider configuration) {
    boolean nodeWasSelected = false;
    C nextNode = null;
    Random random = new Random();
    double randomValue = random.nextDouble();

    AcsConfigurationProvider configurationProvider = (AcsConfigurationProvider) configuration;

    double bestChoiceProbability = configurationProvider
        .getBestChoiceProbability();

    HashMap<C, Double> componentsWithProbabilities = this
        .getComponentsWithProbabilities(environment, configurationProvider);

    if (randomValue < bestChoiceProbability) {

      // TODO(cgavidia): This branch has testing pending.
      nextNode = getMostConvenient(componentsWithProbabilities);

      if (nextNode != null) {
        nodeWasSelected = true;
        getAnt().visitNode(nextNode);
      }

    } else {

      double value = random.nextDouble();
      double total = 0;

      Iterator<Entry<C, Double>> componentWithProbabilitiesIterator = componentsWithProbabilities
          .entrySet().iterator();
      while (componentWithProbabilitiesIterator.hasNext()) {
        Entry<C, Double> componentWithProbability = componentWithProbabilitiesIterator
            .next();

        total += componentWithProbability.getValue();

        if (total >= value) {
          nextNode = componentWithProbability.getKey();
          nodeWasSelected = true;
          getAnt().visitNode(nextNode);
          return;
        }
      }
    }

    if (!nodeWasSelected) {
      this.doIfNoNodeWasSelected(environment, configuration);
    }
  }

  /**
   * Returns the most convenient component based on heuristic and pheromone
   * information.
   * 
   * @param componentsWithProbabilities
   *          Possible components.
   * @return Most convenient component.
   */
  public C getMostConvenient(HashMap<C, Double> componentsWithProbabilities) {
    C nextNode = null;
    double currentMaximumProbability = -1;

    Iterator<Entry<C, Double>> componentWithProbabilitiesIterator = componentsWithProbabilities
        .entrySet().iterator();
    while (componentWithProbabilitiesIterator.hasNext()) {
      Entry<C, Double> componentWithProbability = componentWithProbabilitiesIterator
          .next();

      C possibleMove = componentWithProbability.getKey();
      double currentProbability = componentWithProbability.getValue();

      if (!getAnt().isNodeVisited(possibleMove)
          && currentProbability > currentMaximumProbability) {

        nextNode = possibleMove;
        currentMaximumProbability = currentProbability;
      }
    }
    return nextNode;
  }

  protected void doIfNoNodeWasSelected(E environment,
      ConfigurationProvider configuration) {
    throw new SolutionConstructionException(
        "This policy couldn't select a new component for the current solution. \n"
            + "Partial solution is: " + getAnt().getSolutionAsString());
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
  public HashMap<C, Double> getComponentsWithProbabilities(E environment,
      AcsConfigurationProvider configurationProvider) {
    HashMap<C, Double> componentsWithProbabilities = new HashMap<C, Double>();

    double denominator = 0.0;
    for (C possibleMove : getAnt().getNeighbourhood(environment)) {
      if (!getAnt().isNodeVisited(possibleMove)
          && getAnt().isNodeValid(possibleMove)) {

        Double heuristicTimesPheromone = getHeuristicTimesPheromone(
            environment, configurationProvider, possibleMove);

        denominator += heuristicTimesPheromone;
        componentsWithProbabilities.put(possibleMove, 0.0);
      }
    }

    Iterator<Entry<C, Double>> componentWithProbabilitiesIterator = componentsWithProbabilities
        .entrySet().iterator();
    while (componentWithProbabilitiesIterator.hasNext()) {
      Entry<C, Double> componentWithProbability = componentWithProbabilitiesIterator
          .next();
      C component = componentWithProbability.getKey();

      Double numerator = getHeuristicTimesPheromone(environment,
          configurationProvider, component);
      componentWithProbability.setValue(numerator / denominator);
    }

    if (componentsWithProbabilities.size() < 1) {
      return doIfNoComponentsFound(environment, configurationProvider);
    }

    return componentsWithProbabilities;
  }

  protected HashMap<C, Double> doIfNoComponentsFound(E environment,
      AcsConfigurationProvider configurationProvider) {
    throw new SolutionConstructionException(
        "We have no suitable components to add to the solution from current position. "
            + "Partial solution is: " + getAnt().getSolutionAsString());
  }

  private Double getHeuristicTimesPheromone(E environment,
      AcsConfigurationProvider configurationProvider, C possibleMove) {
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
