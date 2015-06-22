package isula.aco.algorithms.maxmin;

import isula.aco.ACOPhase;
import isula.aco.ACOPolicy;
import isula.aco.AntColony;
import isula.aco.Environment;

import java.util.logging.Logger;

public class UpdatePheromoneMatrixPolicy extends ACOPolicy {

  private static Logger logger = Logger
      .getLogger(UpdatePheromoneMatrixPolicy.class.getName());

  private MaxMinConfigurationProvider configurationProvider;

  public UpdatePheromoneMatrixPolicy(
      MaxMinConfigurationProvider configurationProvider) {
    super(ACOPhase.AFTER_SOLUTION_CONSTRUCTION);

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

    double pheromoneMatrix[][] = environment.getPheromoneMatrix();
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

    // TODO(cgavidia): Incomplete. Port the best Ant Contribution, and the
    // maximum limit restriction.
  }

}
