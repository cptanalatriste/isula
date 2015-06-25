package isula.aco.algorithms.maxmin;

import isula.aco.Ant;
import isula.aco.ConfigurationProvider;
import isula.aco.DaemonAction;
import isula.aco.DaemonActionType;

import java.util.logging.Logger;

//TODO(cgavidia): Generics can be used on Configuration Provider types.
public class UpdatePheromoneMatrixPolicy extends DaemonAction {

  private static Logger logger = Logger
      .getLogger(UpdatePheromoneMatrixPolicy.class.getName());

  /**
   * Instantiates the Update Pheromone Matrix Policy.
   * 
   * @param configurationProvider
   *          Configuration provider.
   */
  public UpdatePheromoneMatrixPolicy() {
    super(DaemonActionType.AFTER_ITERATION_CONSTRUCTION);

  }

  @Override
  public void applyDaemonAction(ConfigurationProvider provider) {

    MaxMinConfigurationProvider configurationProvider = (MaxMinConfigurationProvider) provider;
    logger.info("UPDATING PHEROMONE TRAILS");

    logger.info("Performing evaporation on all edges");
    logger.info("Evaporation ratio: "
        + configurationProvider.getEvaporationRatio());

    // TODO(cgavidia): Copy pasted code from the other policy. I don't implement
    // the data structure access code yet because dependencies on the client
    // project

    double[][] pheromoneMatrix = getEnvironment().getPheromoneMatrix();
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

    Ant bestAnt = getAntColony().getBestPerformingAnt(getEnvironment());
    double contribution = configurationProvider.getQValue()
        / bestAnt.getSolutionQuality(getEnvironment());

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
