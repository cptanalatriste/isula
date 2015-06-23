package isula.aco;

public abstract class Ant {

  private int currentIndex = 0;

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

  public abstract double getSolutionQuality(Environment environment);

  // TODO(cgavidia): Maybe the parameter for this method should be a Policy.
  // Also, evaluate if it is required to be public.
  /**
   * Selects a node and marks it as visited.
   * 
   * @param environment
   *          Environment where the ant is building a solution.
   * @param configurationProvider
   *          Configuration provider.
   */
  public abstract void selectNextNode(Environment environment,
      ConfigurationProvider configurationProvider);

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

}
