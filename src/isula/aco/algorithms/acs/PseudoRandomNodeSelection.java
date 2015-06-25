package isula.aco.algorithms.acs;

import isula.aco.AntPolicy;
import isula.aco.AntPolicyType;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;

import java.util.Random;

public class PseudoRandomNodeSelection extends AntPolicy {

  public PseudoRandomNodeSelection() {
    super(AntPolicyType.NODE_SELECTION);
  }

  @Override
  public void applyPolicy(Environment environment,
      ConfigurationProvider configuration) {
    int nextNode = 0;
    Random random = new Random();
    double randomValue = random.nextDouble();

    AcsConfigurationProvider configurationProvider = (AcsConfigurationProvider) configuration;

    double[][] problemGraph = environment.getProblemGraph();
    double[][] pheromoneMatrix = environment.getPheromoneMatrix();

    double bestChoiceProbability = (problemGraph.length - configurationProvider
        .getBestChoiceConstant()) / problemGraph.length;

    if (randomValue < bestChoiceProbability) {
      double currentMaximumFeromone = -1;

      for (int i = 0; i < problemGraph.length; i++) {
        double currentPheromone = pheromoneMatrix[i][getAnt().getCurrentIndex()];

        if (!getAnt().isNodeVisited(i)
            && currentPheromone > currentMaximumFeromone) {
          nextNode = i;
          currentMaximumFeromone = currentPheromone;
        }

        getAnt().visitNode(nextNode);
      }
    } else {
      double[] probabilities = this.getProbabilitiesVector(environment);
      double value = randomValue;
      double total = 0;

      for (int i = 0; i < problemGraph.length; i++) {
        total += probabilities[i];

        if (total >= value) {
          nextNode = i;
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
  private double[] getProbabilitiesVector(Environment environment) {
    int[] solution = getAnt().getSolution();
    double[][] pheromoneMatrix = environment.getPheromoneMatrix();
    double[] probabilities = new double[solution.length];

    double denominator = 0.0;
    for (int l = 0; l < pheromoneMatrix.length; l++) {
      if (getAnt().isNodeVisited(l)) {
        denominator += pheromoneMatrix[l][getAnt().getCurrentIndex()];
      }
    }

    for (int j = 0; j < solution.length; j++) {
      if (getAnt().isNodeVisited(j)) {
        probabilities[j] = 0.0;
      } else {
        double numerator = pheromoneMatrix[j][getAnt().getCurrentIndex()];
        probabilities[j] = numerator / denominator;
      }
    }

    return probabilities;
  }

}
