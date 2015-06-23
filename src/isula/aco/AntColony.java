package isula.aco;

import isula.aco.exception.MethodNotImplementedException;

import java.util.logging.Logger;

public class AntColony {

  private static Logger logger = Logger.getLogger(AntColony.class.getName());

  private int numberOfAnts;
  private Ant[] hive;

  /**
   * Creates a colony of ants
   * 
   * @param numberOfAnts
   *          Number of ants in the colony.
   */
  public AntColony(int numberOfAnts) {
    this.numberOfAnts = numberOfAnts;

    logger.info("Number of Ants in Colony: " + numberOfAnts);
  }

  /**
   * Initialization code for the colony. The main responsibility is Ant
   * instantiation.
   * 
   */
  public void buildColony() {
    this.hive = new Ant[this.numberOfAnts];

    for (int j = 0; j < hive.length; j++) {
      hive[j] = this.createAnt();
    }
  }

  protected Ant createAnt() {
    throw new MethodNotImplementedException();
  }

  /**
   * Returns the ant with the best performance so far.
   * 
   * @param environment
   *          Environment where the Ants are building solutions.
   * @return Best performing Ant.
   */
  public Ant getBestPerformingAnt(Environment environment) {
    Ant bestAnt = hive[0];

    for (Ant ant : hive) {
      if (ant.getSolutionQuality(environment) < bestAnt
          .getSolutionQuality(environment)) {
        bestAnt = ant;
      }
    }

    return bestAnt;
  }

  public Ant[] getHive() {
    return hive;
  }

  /**
   * Clears solution build for every Ant in the colony.
   */
  public void clearAntSolutions() {
    logger.info("CLEARING ANT SOLUTIONS");

    for (Ant ant : hive) {
      ant.setCurrentIndex(0);
      ant.clear();
    }

  }

  /**
   * Puts every ant in the colony to build a solution.
   * 
   * @param environment
   *          Environment that represents the problem.
   * @param configurationProvider
   *          Configuration provider.
   */
  public void buildSolutions(Environment environment,
      ConfigurationProvider configurationProvider) {
    logger.info("BUILDING ANT SOLUTIONS");

    int antCounter = 0;
    for (Ant ant : hive) {
      logger.info("Current ant: " + antCounter);

      while (!ant.isSolutionReady(environment)) {
        ant.selectNextNode(environment, configurationProvider);
      }

      logger.info("Original Solution > Makespan: "
          + ant.getSolutionQuality(environment) + ", Schedule: "
          + ant.getSolutionAsString());

      ant.improveSolution(environment);

      logger.info("After Local Search > Makespan: "
          + ant.getSolutionQuality(environment) + ", Schedule: "
          + ant.getSolutionAsString());

      antCounter++;
    }
  }

}
