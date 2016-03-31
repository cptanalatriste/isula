package isula.aco;

import isula.aco.exception.ConfigurationException;
import isula.aco.exception.SolutionConstructionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The little workers that build solutions: They belong to a colony. This is an
 * Abstract Type so you must extend it in order to fill the characteristics of
 * your optimization problem.
 * <p>
 * <p>
 * Some convenient methods to define are:
 * <ul>
 * <li>isSolutionReady(), to define when the Ant must stop adding components to
 * its solution.
 * <li>getSolutionCost(), to define the cost of the current solution. It
 * will help to decide the best solution built so far.
 * <li>getHeuristicValue(), to explote problem domain information while
 * constructing solutions.
 * <li>getNeighbourhood(), this returns a list of possible components to add to
 * the solution.
 * <li>getPheromoneTrailValue(), returns the pheromone trail value associated to
 * a Solution Component.
 * <li>setPheromoneTrailValue(), to assign a pheromone value to a Solution
 * Component.
 * </ul>
 *
 * @param <C> Class for components of a solution.
 * @param <E> Class representing the Environment.
 * @author Carlos G. Gavidia
 */
public abstract class Ant<C, E extends Environment> {

    private static final int DONT_CHECK_NUMBERS = -1;
    private static final int ONE_POLICY = 1;

    private int currentIndex = 0;

    private List<AntPolicy<C, E>> policies = new ArrayList<AntPolicy<C, E>>();

    // TODO(cgavidia): Temporarly, we're using an array of items. It will later
    // evolve to an array of solution components, or a List.
    private C[] solution;

    // TODO(cgavidia): This is redundant. Or it should be implemented as another
    // data structure.
    private Map<C, Boolean> visitedComponents = new HashMap<C, Boolean>();

    /**
     * Mark a node as visited.
     *
     * @param visitedNode Visited node.
     */
    public void visitNode(C visitedNode) {
        if (currentIndex < getSolution().length) {

            getSolution()[currentIndex] = visitedNode;
            visitedComponents.put(visitedNode, true);
            currentIndex++;
        } else {
            throw new SolutionConstructionException("Couldn't add component "
                    + visitedNode.toString() + " at index " + currentIndex
                    + ": Solution length is: " + getSolution().length
                    + ". \nPartial solution is " + this.getSolutionAsString());
        }

    }

    /**
     * Resets the visited vector, clears current solution components and sets the
     * index to 0.
     */
    public void clear() {

        this.setCurrentIndex(0);

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
        String solutionString = "";
        for (int i = 0; i < solution.length; i++) {
            if (solution[i] != null) {
                solutionString = solutionString + " " + solution[i].toString();
            }
        }
        return solutionString;
    }

    public void addPolicy(AntPolicy<C, E> antPolicy) {
        this.policies.add(antPolicy);
    }

    AntPolicy<C, E> getAntPolicy(AntPolicyType policyType, int expectedNumber) {
        int numberOfPolicies = 0;
        AntPolicy<C, E> selectedPolicy = null;

        for (AntPolicy<C, E> policy : policies) {
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
     * @param environment           Environment where the ant is building a solution.
     * @param configurationProvider Configuration provider.
     */
    public void selectNextNode(E environment,
                               ConfigurationProvider configurationProvider) {

        AntPolicy<C, E> selectNodePolicity = getAntPolicy(
                AntPolicyType.NODE_SELECTION, ONE_POLICY);

        // TODO(cgavidia): With this approach, the policy is a shared resource
        // between ants. This doesn't allow parallelism.
        selectNodePolicity.setAnt(this);
        selectNodePolicity.applyPolicy(environment, configurationProvider);
    }

    /**
     * Improves the quality of the solution produced.
     *
     * @param environment           Environment where the ant is building a solution.
     * @param configurationProvider Configuration provider.
     */
    public void doAfterSolutionIsReady(E environment,
                                       ConfigurationProvider configurationProvider) {
        AntPolicy<C, E> selectNodePolicity = getAntPolicy(
                AntPolicyType.AFTER_SOLUTION_IS_READY, DONT_CHECK_NUMBERS);

        if (selectNodePolicity != null) {
            // TODO(cgavidia): With this approach, the policy is a shared resource
            // between ants. This doesn't allow parallelism.
            selectNodePolicity.setAnt(this);
            selectNodePolicity.applyPolicy(environment, configurationProvider);
        }
    }

    /**
     * Verifies if a node is visited.
     *
     * @param node Node to verify.
     * @return True if the node is already visited. False otherwise.
     */

    public boolean isNodeVisited(C node) {
        boolean visited = false;

        Boolean inVisitedMap = visitedComponents.get(node);

        if (inVisitedMap != null && inVisitedMap) {
            visited = inVisitedMap;
        }

        return visited;
    }

    public boolean isNodeValid(C node) {
        return true;
    }

    /**
     * Sets the current index for the Ant.
     *
     * @param currentIndex Current index.
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
    public C[] getSolution() {
        return solution;
    }

    public void setSolution(C[] solution) {
        this.solution = solution;
    }

    public Map<C, Boolean> getVisited() {
        return visitedComponents;
    }

    public void setVisited(Map<C, Boolean> visited) {
        this.visitedComponents = visited;
    }

    public abstract List<C> getNeighbourhood(E environment);

    // TODO(cgavidia): Maybe we should move this to Environment.
    public abstract Double getPheromoneTrailValue(C solutionComponent,
                                                  Integer positionInSolution, E environment);

    public abstract Double getHeuristicValue(C solutionComponent,
                                             Integer positionInSolution, E environment);

    public abstract void setPheromoneTrailValue(C solutionComponent,
                                                Integer positionInSolution, E environment, Double value);

    public abstract double getSolutionCost(E environment);

    public abstract boolean isSolutionReady(E environment);

}
