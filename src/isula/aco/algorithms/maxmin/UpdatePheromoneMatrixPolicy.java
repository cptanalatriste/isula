package isula.aco.algorithms.maxmin;

import isula.aco.Ant;
import isula.aco.AntColony;
import isula.aco.AntColonyPhase;
import isula.aco.AntColonyPolicy;
import isula.aco.Environment;

import java.util.logging.Logger;

public class UpdatePheromoneMatrixPolicy extends AntColonyPolicy {

  private static Logger logger = Logger
      .getLogger(UpdatePheromoneMatrixPolicy.class.getName());

  private MaxMinConfigurationProvider configurationProvider;

  /**
   * Instantiates the Update Pheromone Matrix Policy.
   * 
   * @param configurationProvider
   *          Configuration provider.
   */
  public UpdatePheromoneMatrixPolicy(
      MaxMinConfigurationProvider configurationProvider) {
    super(AntColonyPhase.AFTER_SOLUTION_CONSTRUCTION);

    this.configurationProvider = configurationProvider;
  }

  @Override
  public void applyPolicy(Environment environment, AntColony antColony) {
    logger.info("UPDATING PHEROMONE TRAILS");

    logger.info("Performing evaporation on all edges");
    logger.info("Evaporation ratio: "
        + configurationProvider.getEvaporationRatio());

    // TODO(cgavidia): Copy pasted code from the other policy. I don't implement
    // the data structure access code yet because dependencies on the client
    // project

    double[][] pheromoneMatrix = environment.getPheromoneMatrix();
    int matrixRows = pheromoneMatrix.length;
    int matrixColumns = pheromoneMatrix.length;

    for (int i = 0; i < matrixRows; i++) {
      for (int j = 0; j < matrixColumns; j++) {
        double newValue = pheromoneMatrix[i][j]
            * configurationProvider.getEvaporationRatio();

        if (newValue >= configurationProvider.getMinimumPheromoneValue()) {
          pheromoneMatrix[i][j] = newValue;
        } else {
          pheromoneMatrix[i][j] = configurationProvider
              .getMinimumPheromoneValue();
        }
      }
    }

    logger.info("Depositing pheromone on Best Ant trail.");

    Ant bestAnt = antColony.getBestPerformingAnt(environment);
    double contribution = configurationProvider.getQValue()
        / bestAnt.getSolutionQuality(environment);

    logger.info("Contibution for best ant: " + contribution);

    int[] bestSolution = bestAnt.getSolution();

    // TODO(cgavidia): From here, we can factor the policy of only best ant
    // deposits pheromone.
    for (int i = 0; i < bestSolution.length; i++) {
      double newValue = pheromoneMatrix[bestSolution[i]][i] + contribution;

      if (newValue <= configurationProvider.getMaximumPheromoneValue()) {
        pheromoneMatrix[bestSolution[i]][i] = newValue;
      } else {
        pheromoneMatrix[bestSolution[i]][i] = configurationProvider
            .getMaximumPheromoneValue();
      }
    }
  }

}
