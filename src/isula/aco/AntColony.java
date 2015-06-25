package isula.aco;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class AntColony<E> {

  private static Logger logger = Logger.getLogger(AntColony.class.getName());

  private int numberOfAnts;
  private Object[] hive;

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
    this.hive = new Object[this.numberOfAnts];

    for (int j = 0; j < hive.length; j++) {
      hive[j] = this.createAnt();
    }
  }

  protected abstract Ant<E> createAnt();

  /**
   * Returns the ant with the best performance so far.
   * 
   * @param environment
   *          Environment where the Ants are building solutions.
   * @return Best performing Ant.
   */
  @SuppressWarnings("unchecked")
  public Ant<E> getBestPerformingAnt(Environment environment) {
    Ant<E> bestAnt = (Ant<E>) hive[0];

    for (Object antAsObject : hive) {
      Ant<E> ant = (Ant<E>) antAsObject;

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
  @SuppressWarnings("unchecked")
  public List<Ant<E>> getHive() {
    List<Ant<E>> hiveAsList = new ArrayList<Ant<E>>();

    for (Object antAsObject : hive) {
      Ant<E> ant = (Ant<E>) antAsObject;
      hiveAsList.add(ant);
    }
    return hiveAsList;
  }

  /**
   * Clears solution build for every Ant in the colony.
   */
  public void clearAntSolutions() {
    logger.info("CLEARING ANT SOLUTIONS");

    for (Object antAsObject : hive) {
      @SuppressWarnings("unchecked")
      Ant<E> ant = (Ant<E>) antAsObject;

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
    for (Object antAsObject : hive) {
      @SuppressWarnings("unchecked")
      Ant<E> ant = (Ant<E>) antAsObject;

      logger.info("Current ant: " + antCounter);

      while (!ant.isSolutionReady(environment)) {
        ant.selectNextNode(environment, configurationProvider);
      }

      logger.info("Original Solution > Makespan: "
          + ant.getSolutionQuality(environment) + ", Schedule: "
          + ant.getSolutionAsString());

      ant.improveSolution(environment, configurationProvider);

      logger.info("After Local Search > Makespan: "
          + ant.getSolutionQuality(environment) + ", Schedule: "
          + ant.getSolutionAsString());

      antCounter++;
    }
  }

}
