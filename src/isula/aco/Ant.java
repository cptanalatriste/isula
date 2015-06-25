package isula.aco;

import isula.aco.exception.ConfigurationException;

import java.util.ArrayList;
import java.util.List;

public abstract class Ant {

  private static final int DONT_CHECK_NUMBERS = -1;

  private static final int ONE_POLICY = 1;

  private int currentIndex = 0;

  private List<AntPolicy> policies = new ArrayList<AntPolicy>();

  // TODO(cgavidia): Temporarly, we're using an array of items. It will later
  // evolve to an array of solution components, or a List.
  private int[] solution;
  private boolean[] visited;

  /**
   * Mark a node as visited.
   * 
   * @param visitedNode
   *          Visited node.
   */
  public void visitNode(int visitedNode) {
    getSolution()[currentIndex] = visitedNode;
    getVisited()[visitedNode] = true;
    currentIndex++;
  }

  /**
   * Resets the visited vector.
   */
  public void clear() {
    for (int i = 0; i < getVisited().length; i++) {
      visited[i] = false;
    }
  }

  /**
   * Gets th solution built as a String.
   * 
   * @return Solution as a String.
   */
  public String getSolutionAsString() {
    String solutionString = new String();
    for (int i = 0; i < solution.length; i++) {
      solutionString = solutionString + " " + solution[i];
    }
    return solutionString;
  }

  public void addPolicy(AntPolicy antPolicy) {
    antPolicy.setAnt(this);
    this.policies.add(antPolicy);
  }

  private AntPolicy getAntPolicy(AntPolicyType policyType, int expectedNumber) {
    int numberOfPolicies = 0;
    AntPolicy selectedPolicy = null;

    for (AntPolicy policy : policies) {
      if (policyType.equals(policy.getPolicyType())) {
        selectedPolicy = policy;
        numberOfPolicies += 1;
      }
    }

    if (expectedNumber > 0 && numberOfPolicies != expectedNumber) {
      throw new ConfigurationException("The number of " + policyType
          + " policies was " + numberOfPolicies + ". We were expecting "
          + expectedNumber);
    }

    return selectedPolicy;
  }

  /**
   * Selects a node and marks it as visited.
   * 
   * @param environment
   *          Environment where the ant is building a solution.
   * @param configurationProvider
   *          Configuration provider.
   */
  public void selectNextNode(Environment environment,
      ConfigurationProvider configurationProvider) {

    AntPolicy selectNodePolicity = getAntPolicy(AntPolicyType.NODE_SELECTION,
        ONE_POLICY);
    selectNodePolicity.applyPolicy(environment, configurationProvider);
  }

  /**
   * Improves the quality of the solution produced.
   * 
   * @param environment
   *          Environment where the ant is building a solution.
   * @param configurationProvider
   *          Configuration provider.
   */
  public void improveSolution(Environment environment,
      ConfigurationProvider configurationProvider) {
    AntPolicy selectNodePolicity = getAntPolicy(
        AntPolicyType.SOLUTION_IMPROVEMENT, DONT_CHECK_NUMBERS);

    if (selectNodePolicity != null) {
      selectNodePolicity.applyPolicy(environment, configurationProvider);
    }
  }

  /**
   * Verifies if a node is visited.
   * 
   * @param node
   *          Node to verify.
   * @return True if the node is already visited. False otherwise.
   */

  public boolean isNodeVisited(int node) {
    return getVisited()[node];
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

  // TODO(cgavidia): For convenience, we're accesing this data structures
  // directly.
  public int[] getSolution() {
    return solution;
  }

  public void setSolution(int[] solution) {
    this.solution = solution;
  }

  public boolean[] getVisited() {
    return visited;
  }

  public void setVisited(boolean[] visited) {
    this.visited = visited;
  }

  public abstract double getSolutionQuality(Environment environment);

  public abstract boolean isSolutionReady(Environment environment);

}
