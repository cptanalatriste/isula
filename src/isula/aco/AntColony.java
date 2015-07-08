package isula.aco;

import isula.aco.exception.ConfigurationException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AntColony<E> {

  private static Logger logger = Logger.getLogger(AntColony.class.getName());

  private int numberOfAnts;
  private List<Ant<E>> hive = new ArrayList<Ant<E>>();

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
  public void buildColony(Environment environment) {
    for (int j = 0; j < numberOfAnts; j++) {
      hive.add(this.createAnt(environment));
    }
  }

  protected abstract Ant<E> createAnt(Environment environment);

  /**
   * Returns the ant with the best performance so far.
   * 
   * @param environment
   *          Environment where the Ants are building solutions.
   * @return Best performing Ant.
   */
  public Ant<E> getBestPerformingAnt(Environment environment) {
    Ant<E> bestAnt = hive.get(0);

    for (Ant<E> ant : hive) {

      if (ant.getSolutionQuality(environment) < bestAnt
          .getSolutionQuality(environment)) {
        bestAnt = ant;
      }
    }

    return bestAnt;
  }

  /**
   * Returns a List of all the ants in the colony.
   * 
   * @return List of Ants.
   */
  public List<Ant<E>> getHive() {
    return hive;
  }

  /**
   * Clears solution build for every Ant in the colony.
   */
  public void clearAntSolutions() {
    logger.log(Level.FINE, "CLEARING ANT SOLUTIONS");

    for (Ant<E> ant : hive) {
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
    logger.log(Level.FINE, "BUILDING ANT SOLUTIONS");

    int antCounter = 0;

    if (hive.size() == 0) {
      throw new ConfigurationException(
          "Your colony is empty: You have no ants to solve the problem. "
              + "Have you called the buildColony() method?");
    }

    for (Ant<E> ant : hive) {
      logger.info("Current ant: " + antCounter);

      while (!ant.isSolutionReady(environment)) {
        ant.selectNextNode(environment, configurationProvider);
      }

      ant.doAfterSolutionIsReady(environment, configurationProvider);

      logger.log(Level.FINE, "Solution is ready > Quality: "
          + ant.getSolutionQuality(environment) + ", Solution: "
          + ant.getSolutionAsString());

      antCounter++;
    }
  }

  public int getNumberOfAnts() {
    return numberOfAnts;
  }

  public void setNumberOfAnts(int numberOfAnts) {
    this.numberOfAnts = numberOfAnts;
  }

}
