package isula.aco.problems.flowshop;

import isula.aco.Ant;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;
import isula.aco.algorithms.acs.AcsConfigurationProvider;
import isula.aco.algorithms.acs.PseudoRandomNodeSelection;

/**
 * An Ant that belongs to Colony in the context of ACO.
 * 
 * @author Carlos G. Gavidia (cgavidia@acm.org)
 * @author Adrian Pareja (adrian@pareja.com)
 */
public class AntForFlowShop extends Ant {

  // TODO(cgavidia): Maybe a more generic parameter would be an Environment
  // instance.
  /**
   * Creates an Ant specialized in the Flow Shop Scheduling problem.
   * 
   * @param graphLenght
   *          Number of rows of the Problem Graph.
   */
  public AntForFlowShop(int graphLenght) {
    super();
    this.setSolution(new int[graphLenght]);
    this.setVisited(new boolean[graphLenght]);
  }

  @Override
  public boolean isSolutionReady(Environment environment) {
    FlowShopEnvironment flowShopEnvironment = (FlowShopEnvironment) environment;
    return getCurrentIndex() == flowShopEnvironment.getNumberOfJobs();
  }

  /**
   * Gets the makespan of the Ants built solution.
   * 
   * @param graph
   *          Problem graph.
   * @return Makespan of the solution.
   */
  @Override
  public double getSolutionQuality(Environment environment) {
    return getScheduleMakespan(getSolution(), environment.getProblemGraph());
  }

  @Override
  public void selectNextNode(Environment environment,
      ConfigurationProvider configurationProvider) {

    // TODO(cgavidia): Policies should be grouped and classified. Maybe a Policy
    // super type? An enum?
    PseudoRandomNodeSelection pseudoRandomNodeSelection = new PseudoRandomNodeSelection(
        (AcsConfigurationProvider) configurationProvider);
    pseudoRandomNodeSelection.applyPolicy(environment, this);
  }

  /**
   * Applies local search to the solution built.
   * 
   * @param environment
   *          Environment where the Ant is building a solution.
   */
  @Override
  public void improveSolution(Environment environment) {
    // TODO(cgavidia): Again, this set of policies should be configurable.
    LocalSearchPolicy localSearchPolicy = new LocalSearchPolicy();
    localSearchPolicy.applyPolicy(environment, this);
  }

  /**
   * Calculates the MakeSpan for the generated schedule.
   * 
   * @param schedule
   *          Schedule
   * @param jobInfo
   *          Job Info.
   * @return Makespan.
   */
  public double getScheduleMakespan(int[] schedule, double[][] jobInfo) {
    int machines = jobInfo[0].length;
    double[] machinesTime = new double[machines];
    double tiempo = 0;

    for (int job : schedule) {
      for (int i = 0; i < machines; i++) {
        tiempo = jobInfo[job][i];
        if (i == 0) {
          machinesTime[i] = machinesTime[i] + tiempo;
        } else {
          if (machinesTime[i] > machinesTime[i - 1]) {
            machinesTime[i] = machinesTime[i] + tiempo;
          } else {
            machinesTime[i] = machinesTime[i - 1] + tiempo;
          }
        }
      }
    }
    return machinesTime[machines - 1];
  }

}