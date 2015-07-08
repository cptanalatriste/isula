package isula.aco;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.ConfigurationException;

public class AcoProblemSolver<E> {

  private static Logger logger = Logger.getLogger(AcoProblemSolver.class
      .getName());

  public E[] bestSolution;
  public double bestSolutionQuality = -1.0;
  public String bestSolutionAsString = "";

  private Environment environment;
  private AntColony<E> antColony;

  // TODO(cgavidia): Maybe we should handle a list of configuration providers.
  private ConfigurationProvider configurationProvider;

  private List<DaemonAction<E>> daemonActions = new ArrayList<DaemonAction<E>>();

  /**
   * Adds a Daemon Action for the current solver.
   * 
   * @param daemonAction
   *          Daemon action.
   */
  public void addDaemonAction(DaemonAction<E> daemonAction) {

    daemonAction.setAntColony(antColony);
    daemonAction.setEnvironment(environment);
    daemonActions.add(daemonAction);
  }

  /**
   * Solves an optimization problem using a Colony of Ants.
   * 
   * @throws ConfigurationException
   *           If algorithm parameters aren't properly configured.
   */
  public void solveProblem() throws ConfigurationException {
    logger.info("Starting computation at: " + new Date());
    final long startTime = System.nanoTime();

    applyDaemonActions(DaemonActionType.INITIAL_CONFIGURATION);

    logger.info("STARTING ITERATIONS");
    int numberOfIterations = configurationProvider.getNumberOfIterations();

    if (numberOfIterations < 1) {
      throw new ConfigurationException(
          "No iterations are programed for this solver. Check your Configuration Provider.");
    }

    logger.info("Number of iterations: " + numberOfIterations);

    int iteration = 0;

    while (iteration < numberOfIterations) {
      logger.info("Current iteration: " + iteration);

      antColony.clearAntSolutions();
      antColony.buildSolutions(environment, configurationProvider);

      // TODO(cgavidia): This should reference the Update Pheromone routine.
      // Maybe with the Policy hierarchy.
      applyDaemonActions(DaemonActionType.AFTER_ITERATION_CONSTRUCTION);

      updateBestSolution(environment);
      iteration++;
    }

    logger.info("Finishing computation at: " + new Date());
    long endTime = System.nanoTime();
    double executionTime = (endTime - startTime) / 1000000000.0;
    logger.info("Duration (in seconds): " + executionTime);

    logger.info("EXECUTION FINISHED");
    logger.info("Best solution quality: " + bestSolutionQuality);
    logger.info("Best solution:" + bestSolutionAsString);

  }

  private void updateBestSolution(Environment environment) {
    logger.log(Level.FINE, "GETTING BEST SOLUTION FOUND");

    Ant<E> bestAnt = antColony.getBestPerformingAnt(environment);

    if (bestSolution == null
        || bestSolutionQuality > bestAnt.getSolutionQuality(environment)) {
      bestSolution = bestAnt.getSolution().clone();
      bestSolutionQuality = bestAnt.getSolutionQuality(environment);
      bestSolutionAsString = bestAnt.getSolutionAsString();

      logger.info("Best solution so far > Quality: " + bestSolutionQuality
          + ", Solution: " + bestSolutionAsString);

    }

  }

  /**
   * Applies all daemon actions of a specific type.
   * 
   * @param daemonActionType
   *          Daemon action type.
   */
  public void applyDaemonActions(DaemonActionType daemonActionType) {
    for (DaemonAction<E> daemonAction : daemonActions) {
      if (daemonActionType.equals(daemonAction.getAcoPhase())) {
        daemonAction.applyDaemonAction(this.getConfigurationProvider());
      }
    }
  }

  public Environment getEnvironment() {
    return environment;
  }

  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  public AntColony<E> getAntColony() {
    return antColony;
  }

  public void setAntColony(AntColony<E> antColony) {
    this.antColony = antColony;
  }

  public ConfigurationProvider getConfigurationProvider() {
    return configurationProvider;
  }

  public void setConfigurationProvider(
      ConfigurationProvider configurationProvider) {
    this.configurationProvider = configurationProvider;
  }

  public E[] getBestSolution() {
    return bestSolution;
  }

  public double getBestSolutionQuality() {
    return bestSolutionQuality;
  }

  public String getBestSolutionAsString() {
    return bestSolutionAsString;
  }

}
