package isula.aco;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.ConfigurationException;

/**
 * The main component of the framework: Is the one in charge of making a colony
 * an ants to traverse an environment in order to generate solutions.
 *
 * <p>The solveProblem() method is the one that starts the optimization process.
 * Previously, you have to properly configure your solver by assigning it a
 * Colony, an Environment and Daemon Actions (if required).
 * 
 * @author Carlos G. Gavidia
 *
 * @param <C>
 *          Class for components of a solution.
 * @param <E>
 *          Class representing the Environment.
 */
public class AcoProblemSolver<C, E extends Environment> {

  private static Logger logger = Logger.getLogger(AcoProblemSolver.class
      .getName());

  public C[] bestSolution;
  public double bestSolutionCost = 0.0;
  public String bestSolutionAsString = "";

  private E environment;
  private AntColony<C, E> antColony;

  // TODO(cgavidia): Maybe we should handle a list of configuration providers.
  private ConfigurationProvider configurationProvider;

  private List<DaemonAction<C, E>> daemonActions = new ArrayList<DaemonAction<C, E>>();

  /**
   * Adds a list of Daemon Actions for the current solver.
   * 
   * @param daemonActions
   *          Daemon actions.
   */
  @SafeVarargs
  public void addDaemonActions(DaemonAction<C, E>... daemonActions) {
    for (DaemonAction<C, E> daemonAction : daemonActions) {
      this.addDaemonAction(daemonAction);
    }
  }

  /**
   * Adds a Daemon Action for the current solver.
   * 
   * @param daemonAction
   *          Daemon action.
   */
  private void addDaemonAction(DaemonAction<C, E> daemonAction) {

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
    logger.info("Best solution cost: " + bestSolutionCost);
    logger.info("Best solution:" + bestSolutionAsString);

  }

  /**
   * Updates the information of the best solution produced with the solutions
   * produced by the Colony.
   * 
   * @param environment
   *          Environment where the solutions where produced.
   */
  public void updateBestSolution(E environment) {
    logger.log(Level.FINE, "GETTING BEST SOLUTION FOUND");

    Ant<C, E> bestAnt = antColony.getBestPerformingAnt(environment);

    if (bestSolution == null
        || bestSolutionCost > bestAnt.getSolutionCost(environment)) {
      bestSolution = bestAnt.getSolution().clone();
      bestSolutionCost = bestAnt.getSolutionCost(environment);
      bestSolutionAsString = bestAnt.getSolutionAsString();

      logger.info("Best solution so far > Cost: " + bestSolutionCost
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
    for (DaemonAction<C, E> daemonAction : daemonActions) {
      if (daemonActionType.equals(daemonAction.getAcoPhase())) {
        daemonAction.applyDaemonAction(this.getConfigurationProvider());
      }
    }
  }

  public E getEnvironment() {
    return environment;
  }

  public void setEnvironment(E environment) {
    this.environment = environment;
  }

  public AntColony<C, E> getAntColony() {
    return antColony;
  }

  public void setAntColony(AntColony<C, E> antColony) {
    this.antColony = antColony;
  }

  public ConfigurationProvider getConfigurationProvider() {
    return configurationProvider;
  }

  public void setConfigurationProvider(
      ConfigurationProvider configurationProvider) {
    this.configurationProvider = configurationProvider;
  }

  public C[] getBestSolution() {
    return bestSolution;
  }

  public double getBestSolutionCost() {
    return bestSolutionCost;
  }

  public String getBestSolutionAsString() {
    return bestSolutionAsString;
  }

  public void setBestSolutionCost(double bestSolutionCost) {
    this.bestSolutionCost = bestSolutionCost;
  }

}
