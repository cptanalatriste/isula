package isula.aco.algorithms.acs;

import isula.aco.Ant;
import isula.aco.AntPhase;
import isula.aco.AntPolicy;
import isula.aco.Environment;

import java.util.Random;

public class PseudoRandomNodeSelection extends AntPolicy {

  private AcsConfigurationProvider configurationProvider;

  public PseudoRandomNodeSelection(
      AcsConfigurationProvider configurationProvider) {
    super(AntPhase.SOLUTION_CONSTRUCTION);
    this.configurationProvider = configurationProvider;
  }

  @Override
  public void applyPolicy(Environment environment, Ant ant) {
    int nextNode = 0;
    Random random = new Random();
    double randomValue = random.nextDouble();

    double[][] problemGraph = environment.getProblemGraph();
    double[][] pheromoneMatrix = environment.getPheromoneMatrix();

    double bestChoiceProbability = (problemGraph.length - configurationProvider
        .getBestChoiceConstant()) / problemGraph.length;

    if (randomValue < bestChoiceProbability) {
      double currentMaximumFeromone = -1;

      for (int i = 0; i < problemGraph.length; i++) {
        double currentPheromone = pheromoneMatrix[i][ant.getCurrentIndex()];

        if (!ant.isNodeVisited(i) && currentPheromone > currentMaximumFeromone) {
          nextNode = i;
          currentMaximumFeromone = currentPheromone;
        }

        ant.visitNode(nextNode);
      }
    } else {
      double[] probabilities = this.getProbabilitiesVector(environment, ant);
      double value = randomValue;
      double total = 0;

      for (int i = 0; i < problemGraph.length; i++) {
        total += probabilities[i];

        if (total >= value) {
          nextNode = i;
          ant.visitNode(nextNode);
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
   * @param ant
   * @return
   */
  private double[] getProbabilitiesVector(Environment environment, Ant ant) {
    int[] solution = ant.getSolution();
    double[][] pheromoneMatrix = environment.getPheromoneMatrix();
    double[] probabilities = new double[solution.length];

    double denominator = 0.0;
    for (int l = 0; l < pheromoneMatrix.length; l++) {
      if (ant.isNodeVisited(l)) {
        denominator += pheromoneMatrix[l][ant.getCurrentIndex()];
      }
    }

    for (int j = 0; j < solution.length; j++) {
      if (ant.isNodeVisited(j)) {
        probabilities[j] = 0.0;
      } else {
        double numerator = pheromoneMatrix[j][ant.getCurrentIndex()];
        probabilities[j] = numerator / denominator;
      }
    }

    return probabilities;
  }

}
