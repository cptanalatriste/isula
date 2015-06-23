package isula.aco.problems.flowshop;

import isula.aco.Ant;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;
import isula.aco.algorithms.acs.AcsConfigurationProvider;
import isula.aco.algorithms.acs.PseudoRandomNodeSelection;

import java.util.ArrayList;
import java.util.List;

//TODO(cgavidia): This class requires major refactoring yet.

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

  /**
   * Resets the visited vector.
   */
  public void clear() {
    for (int i = 0; i < getVisited().length; i++) {
      getVisited()[i] = false;

    }
  }

  /**
   * Gets the makespan of the Ants built solution.
   * 
   * @param graph
   *          Problem graph.
   * @return Makespan of the solution.
   */
  public double getSolutionQuality(Environment environment) {
    return getScheduleMakespan(getSolution(), environment.getProblemGraph());
  }

  /**
   * Applies local search to the solution built.
   * 
   * @param environment
   *          Environment where the Ant is building a solution.
   */
  public void improveSolution(Environment environment) {
    double makespan = getSolutionQuality(environment);

    int[] localSolutionJobs = new int[getSolution().length];
    List<Integer> jobsList = new ArrayList<Integer>();

    for (int job : getSolution()) {
      jobsList.add(job);
    }

    List<Integer> localSolution = jobsList;

    int indexI = 0;
    boolean lessMakespan = true;

    while (indexI < (getSolution().length) && lessMakespan) {
      int jobI = localSolution.get(indexI);

      localSolution.remove(indexI);

      int indexJ = 0;

      while (indexJ < getSolution().length && lessMakespan) {
        localSolution.add(indexJ, jobI);

        int[] intermediateSolution = new int[getSolution().length];
        int anotherIndex = 0;
        for (int sol : localSolution) {
          intermediateSolution[anotherIndex] = sol;
          anotherIndex++;
        }

        double newMakespan = getScheduleMakespan(intermediateSolution,
            environment.getProblemGraph());

        if (newMakespan < makespan) {
          makespan = newMakespan;
          lessMakespan = false;
        } else {
          localSolution.remove(indexJ);
        }

        indexJ++;
      }
      if (lessMakespan) {
        localSolution.add(indexI, jobI);
      }
      indexI++;
    }

    int index = 0;
    for (int job : localSolution) {
      localSolutionJobs[index] = job;
      index++;
    }
    this.setSolution(localSolutionJobs);
  }

  /**
   * Gets th solution built as a String.
   * 
   * @return Solution as a String.
   */
  public String getSolutionAsString() {
    String solutionString = new String();
    for (int i = 0; i < getSolution().length; i++) {
      solutionString = solutionString + " " + getSolution()[i];
    }
    return solutionString;
  }

  // TODO(cgavidia): Major refactoring here also. Comes from an utility class.
  /**
   * Calculates the MakeSpan for the generated schedule.
   * 
   * @param schedule
   *          Schedule
   * @param jobInfo
   *          Job Info.
   * @return Makespan.
   */
  public static double getScheduleMakespan(int[] schedule, double[][] jobInfo) {
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

  @Override
  public void selectNextNode(Environment environment,
      ConfigurationProvider configurationProvider) {

    // TODO(cgavidia): Policies should be grouped and classified. Maybe a Policy
    // super type? An enum?
    PseudoRandomNodeSelection pseudoRandomNodeSelection = new PseudoRandomNodeSelection(
        (AcsConfigurationProvider) configurationProvider);
    pseudoRandomNodeSelection.applyPolicy(environment, this);
  }
}