package isula.aco.problems.flowshop;

import isula.aco.Ant;
import isula.aco.AntColony;

public class FlowShopAntColony extends AntColony {

  private int numberOfJobs;

  /**
   * Creates an Ant Colony to solve the Flow Shop Scheduling problem.
   * 
   * @param numberOfAnts
   *          Ants for the colony.
   * @param numberOfJobs
   *          Number of jobs.
   */
  public FlowShopAntColony(int numberOfAnts, int numberOfJobs) {
    super(numberOfAnts);

    this.numberOfJobs = numberOfJobs;
  }

  @Override
  protected Ant createAnt() {
    return new AntForFlowShop(numberOfJobs);
  }

}
