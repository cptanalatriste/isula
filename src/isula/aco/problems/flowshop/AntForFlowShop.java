package isula.aco.problems.flowshop;

import isula.aco.Ant;
import isula.aco.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An Ant that belongs to Colony in the context of ACO.
 * 
 * @author Carlos G. Gavidia (cgavidia@acm.org)
 * @author Adrian Pareja (adrian@pareja.com)
 * 
 */

// TODO(cgavidia): This class requires major refactoring yet.
public class AntForFlowShop extends Ant {

  private int currentIndex = 0;

  // private int solution[];
  // public boolean visited[];

  public AntForFlowShop(int graphLenght) {

    super();

    this.setSolution(new int[graphLenght]);
    this.setVisited(new boolean[graphLenght]);
  }

  /**
   * Mark a node as visited.
   * 
   * @param visitedNode
   *          Visited node.
   */
  // TODO(cgavidia): Move to base class
  public void visitNode(int visitedNode) {
    getSolution()[currentIndex] = visitedNode;
    getVisited()[visitedNode] = true;
    currentIndex++;
  }

  /**
   * Verifies if a node is visited.
   * 
   * @param node
   *          Node to verify.
   * @return True if the node is already visited. False otherwise.
   */
  // TODO(cgavidia): Move to base class

  public boolean isNodeVisited(int node) {
    return getVisited()[node];
  }

  /**
   * Selects the next node to move while building a solution,
   * 
   * @param trails
   *          Pheromone matrix.
   * @param graph
   *          Problem graph.
   * @return Next node to move.
   */
  // TODO(cgavidia): Move to base class. As abstract and including policies.

  public int selectNextNode(double[][] trails, double[][] graph) {
    int nextNode = 0;
    Random random = new Random();
    double randomValue = random.nextDouble();
    // Probability Setting from Paper
    double bestChoiceProbability = ((double) graph.length - 4) / graph.length;
    if (randomValue < bestChoiceProbability) {
      double currentMaximumFeromone = -1;
      for (int i = 0; i < graph.length; i++) {
	double currentFeromone = trails[i][currentIndex];
	if (!isNodeVisited(i) && currentFeromone > currentMaximumFeromone) {
	  nextNode = i;
	  currentMaximumFeromone = currentFeromone;
	}
      }
      return nextNode;
    } else {
      double probabilities[] = getProbabilities(trails);
      double r = randomValue;
      double total = 0;
      for (int i = 0; i < graph.length; i++) {
	total += probabilities[i];
	if (total >= r) {
	  nextNode = i;
	  return nextNode;
	}
      }
    }
    return nextNode;
  }

  /**
   * Gets a probabilities vector, containing probabilities to move to each node
   * according to pheromone matrix.
   * 
   * @param trails
   *          Pheromone matrix.
   * @return Probabilities vector.
   */
  // TODO(cgavidia): Move to base class
  private double[] getProbabilities(double[][] trails) {
    double probabilities[] = new double[getSolution().length];

    double denom = 0.0;
    for (int l = 0; l < trails.length; l++) {
      if (!isNodeVisited(l)) {
	denom += trails[l][currentIndex];

      }
    }

    for (int j = 0; j < getSolution().length; j++) {
      if (isNodeVisited(j)) {
	probabilities[j] = 0.0;
      } else {
	double numerator = trails[j][currentIndex];
	probabilities[j] = numerator / denom;
      }
    }
    return probabilities;
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
   * Sets the current index for the Ant.
   * 
   * @param currentIndex
   *          Current index.
   */
  public void setCurrentIndex(int currentIndex) {
    this.currentIndex = currentIndex;
  }

  /**
   * Gets the current index for the Ant.
   * 
   * @return Current index.
   */
  public int getCurrentIndex() {
    return currentIndex;
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
   * @param graph
   *          Problem graph.
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
	int t = 0;
	for (int sol : localSolution) {
	  intermediateSolution[t] = sol;
	  t++;
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

    int k = 0;
    for (int job : localSolution) {
      localSolutionJobs[k] = job;
      k++;
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
}