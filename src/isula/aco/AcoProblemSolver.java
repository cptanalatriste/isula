package isula.aco;

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

  /**
   * Solves an optimization problem using a Colony of Ants.
   */
  public void solveProblem() {
    applyInitialConfigurationPolicies();

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
      applyAfterSolutionConstructionPolicies();
      updateBestSolution(environment);

      iteration++;
    }

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

  public abstract void applyInitialConfigurationPolicies();

  public abstract void applyAfterSolutionConstructionPolicies();

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
