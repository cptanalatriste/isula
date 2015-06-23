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
   * @return
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

}
