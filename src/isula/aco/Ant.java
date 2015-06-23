package isula.aco;

public abstract class Ant {

  // TODO(cgavidia): Temporarly, we're using an array of items. It will later
  // evolve to an array of solution components, or a List.
  private int[] solution;

  private boolean[] visited;

  public abstract double getSolutionQuality(Environment environment);

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
