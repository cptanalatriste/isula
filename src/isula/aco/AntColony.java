package isula.aco;

import java.util.logging.Logger;

public class AntColony {

  private static Logger logger = Logger.getLogger(AntColony.class.getName());

  private int numberOfAnts;
  private Ant[] hive;

  public AntColony(int numberOfAnts) {
    this.numberOfAnts = numberOfAnts;

    logger.info("Number of Ants in Colony: " + numberOfAnts);
  }

  public void buildColony() {
    this.hive = new Ant[this.numberOfAnts];

    for (int j = 0; j < hive.length; j++) {
      hive[j] = this.createAnt();
    }
  }

  protected Ant createAnt() {
    return new Ant();
  }

  public Ant[] getHive() {
    return hive;
  }

}
