package isula.aco.problems.flowshop;

import isula.aco.Ant;
import isula.aco.AntColony;

public class FlowShopAntColony extends AntColony {

  private int numberOfJobs;

  public FlowShopAntColony(int numberOfAnts, int numberOfJobs) {
    super(numberOfAnts);

    this.numberOfJobs = numberOfJobs;
  }

  @Override
  protected Ant createAnt() {
    return new AntForFlowShop(numberOfJobs);
  }

}
