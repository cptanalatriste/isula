package isula.aco;

import isula.aco.exception.ConfigurationException;
import isula.aco.exception.SolutionConstructionException;

import java.util.ArrayList;
import java.util.List;

public abstract class Ant<E> {

  private static final int DONT_CHECK_NUMBERS = -1;
  private static final int ONE_POLICY = 1;

  private int currentIndex = 0;

  private List<AntPolicy<E>> policies = new ArrayList<AntPolicy<E>>();

  // TODO(cgavidia): Temporarly, we're using an array of items. It will later
  // evolve to an array of solution components, or a List.
  private E[] solution;
  private List<E> visitedComponents = new ArrayList<E>();

  /**
   * Mark a node as visited.
   * 
   * @param visitedNode
   *          Visited node.
   */
  public void visitNode(E visitedNode) {
    if (currentIndex < getSolution().length) {

      getSolution()[currentIndex] = visitedNode;
      visitedComponents.add(visitedNode);
      currentIndex++;
    } else {
      throw new SolutionConstructionException("Couldn't add component "
          + visitedNode.toString() + " at index " + currentIndex
          + ": Solution length is: " + getSolution().length
          + ". \nPartial solution is " + this.getSolutionAsString());
    }

  }

  /**
   * Resets the visited vector.
   */
  public void clear() {

    for (int i = 0; i < getSolution().length; i++) {
      getSolution()[i] = null;
    }

    visitedComponents.clear();
  }

  /**
   * Gets th solution built as a String.
   * 
   * @return Solution as a String.
   */
  public String getSolutionAsString() {
    String solutionString = new String();
    for (int i = 0; i < solution.length; i++) {
      if (solution[i] != null) {
        solutionString = solutionString + " " + solution[i].toString();
      }
    }
    return solutionString;
  }

  public void addPolicy(AntPolicy<E> antPolicy) {
    this.policies.add(antPolicy);
  }

  private AntPolicy<E> getAntPolicy(AntPolicyType policyType, int expectedNumber) {
    int numberOfPolicies = 0;
    AntPolicy<E> selectedPolicy = null;

    for (AntPolicy<E> policy : policies) {
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

    AntPolicy<E> selectNodePolicity = getAntPolicy(
        AntPolicyType.NODE_SELECTION, ONE_POLICY);

    // TODO(cgavidia): With this approach, the policy is a shared resource
    // between ants
    selectNodePolicity.setAnt(this);
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
  public void doAfterSolutionIsReady(Environment environment,
      ConfigurationProvider configurationProvider) {
    AntPolicy<E> selectNodePolicity = getAntPolicy(
        AntPolicyType.AFTER_SOLUTION_IS_READY, DONT_CHECK_NUMBERS);

    if (selectNodePolicity != null) {
      selectNodePolicity.setAnt(this);
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

  public boolean isNodeVisited(E node) {

    for (E solutionComponent : visitedComponents) {
      if (node.equals(solutionComponent)) {
        return true;
      }
    }

    return false;
  }

  public boolean isNodeValid(E node) {
    return true;
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
  public E[] getSolution() {
    return solution;
  }

  public void setSolution(E[] solution) {
    this.solution = solution;
  }

  public List<E> getVisited() {
    return visitedComponents;
  }

  public void setVisited(List<E> visited) {
    this.visitedComponents = visited;
  }

  public abstract List<E> getNeighbourhood(Environment environment);

  // TODO(cgavidia): Maybe we should move this to Environment.
  public abstract Double getPheromoneTrailValue(E solutionComponent,
      Integer positionInSolution, Environment environment);

  public abstract Double getHeuristicValue(E solutionComponent,
      Integer positionInSolution, Environment environment);

  public abstract void setPheromoneTrailValue(E solutionComponent,
      Environment environment, Double value);

  public abstract double getSolutionQuality(Environment environment);

  public abstract boolean isSolutionReady(Environment environment);

}
