package isula.aco;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public abstract class AcoProblemSolver {

  private static Logger logger = Logger.getLogger(AcoProblemSolver.class
      .getName());

  public int[] bestSolution;
  public double bestSolutionQuality = -1.0;
  public String bestSolutionAsString = "";

  private Environment environment;
  private AntColony antColony;
  private ConfigurationProvider configurationProvider;

  private List<DaemonAction> daemonActions = new ArrayList<DaemonAction>();

  /**
   * Adds a Daemon Action for the current solver.
   * 
   * @param daemonAction
   *          Daemon action.
   */
  public void addDaemonAction(DaemonAction daemonAction) {

    daemonAction.setAntColony(antColony);
    daemonAction.setEnvironment(environment);
    daemonActions.add(daemonAction);
  }

  /**
   * Solves an optimization problem using a Colony of Ants.
   */
  public void solveProblem() {
    logger.info("Starting computation at: " + new Date());
    final long startTime = System.nanoTime();

    applyDaemonActions(DaemonActionType.INITIAL_CONFIGURATION);

    int iteration = 0;

    logger.info("STARTING ITERATIONS");
    logger.info("Number of iterations: "
        + configurationProvider.getNumberOfIterations());

    while (iteration < configurationProvider.getNumberOfIterations()) {
      logger.info("Current iteration: " + iteration);

      antColony.clearAntSolutions();
      antColony.buildSolutions(environment, configurationProvider);

      // TODO(cgavidia): This should reference the Update Pheromone routine.
      // Maybe with the Policy hierarchy.
      applyDaemonActions(DaemonActionType.AFTER_ITERATION_CONSTRUCTION);

      updateBestSolution(environment);
      iteration++;
    }

    System.out.println("Finishing computation at: " + new Date());
    long endTime = System.nanoTime();
    double executionTime = (endTime - startTime) / 1000000000.0;
    System.out.println("Duration (in seconds): " + executionTime);

    logger.info("EXECUTION FINISHED");
    logger.info("Best schedule makespam: " + bestSolutionQuality);
    logger.info("Best schedule:" + bestSolutionAsString);

  }

  private void updateBestSolution(Environment environment) {
    logger.info("GETTING BEST SOLUTION FOUND");

    Ant bestAnt = antColony.getBestPerformingAnt(environment);

    if (bestSolution == null
        || bestSolutionQuality > bestAnt.getSolutionQuality(environment)) {
      bestSolution = bestAnt.getSolution().clone();
      bestSolutionQuality = bestAnt.getSolutionQuality(environment);
      bestSolutionAsString = bestAnt.getSolutionAsString();

      logger.info("Best solution so far > Makespan: " + bestSolutionQuality
          + ", Schedule: " + bestSolutionAsString);

    }

  }

  /**
   * Applies all daemon actions of a specific type.
   * 
   * @param daemonActionType
   *          Daemon action type.
   */
  public void applyDaemonActions(DaemonActionType daemonActionType) {
    for (DaemonAction daemonAction : daemonActions) {
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

  public AntColony getAntColony() {
    return antColony;
  }

  public void setAntColony(AntColony antColony) {
    this.antColony = antColony;
  }

  public ConfigurationProvider getConfigurationProvider() {
    return configurationProvider;
  }

  public void setConfigurationProvider(
      ConfigurationProvider configurationProvider) {
    this.configurationProvider = configurationProvider;
  }

  public int[] getBestSolution() {
    return bestSolution;
  }

  public double getBestSolutionQuality() {
    return bestSolutionQuality;
  }

  public String getBestSolutionAsString() {
    return bestSolutionAsString;
  }

}
