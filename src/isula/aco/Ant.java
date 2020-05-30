package isula.aco;

import isula.aco.exception.ConfigurationException;
import isula.aco.exception.SolutionConstructionException;

import java.util.*;

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

    private List<AntPolicy<C, E>> policies = new ArrayList<>();

    private List<C> solution;

    // TODO(cgavidia): This is redundant. Or it should be implemented as another
    // data structure.
    private Map<C, Boolean> visitedComponents = new HashMap<>();


    /**
     * Mark a node as visited.
     *
     * @param visitedNode Visited node.
     */
    public void visitNode(C visitedNode, E environment) {
        if (getSolution() != null) {
            getSolution().add(visitedNode);
            visitedComponents.put(visitedNode, true);
            currentIndex++;
        } else {
            throw new SolutionConstructionException("Couldn't add component "
                    + visitedNode.toString() + " at index " + currentIndex
                    + ". \nPartial solution is " + this.getSolutionAsString());
        }

    }

    /**
     * Resets the visited vector, clears current solution components and sets the
     * index to 0.
     */
    public void clear() {

        this.setCurrentIndex(0);

        if (getSolution() == null) {
            throw new SolutionConstructionException("Couldn't clear solution since current solution is null. Verify" +
                    " each ant instance have the solution array properly initialized.");
        }

        getSolution().clear();
        visitedComponents.clear();
    }

    /**
     * Gets th solution built as a String.
     *
     * @return Solution as a String.
     */
    public String getSolutionAsString() {
        StringBuilder solutionString = new StringBuilder();
        for (C c : solution) {
            if (c != null) {
                solutionString.append(" ").append(c.toString());
            }
        }
        return solutionString.toString();
    }

    public void addPolicy(AntPolicy<C, E> antPolicy) {
        this.policies.add(antPolicy);
    }

    List<AntPolicy<C, E>> getAntPolicies(AntPolicyType policyType, int expectedNumber) {
        List<AntPolicy<C, E>> selectedPolicies = new ArrayList<>();

        for (AntPolicy<C, E> policy : policies) {
            if (policyType.equals(policy.getPolicyType())) {
                selectedPolicies.add(policy);
            }
        }

        if (expectedNumber > 0 && selectedPolicies.size() != expectedNumber) {
            throw new ConfigurationException("The number of " + policyType
                    + " policies was " + selectedPolicies.size() + ". We were expecting "
                    + expectedNumber);
        }

        return selectedPolicies;
    }

    /**
     * Selects a node and marks it as visited.
     *
     * @param environment           Environment where the ant is building a solution.
     * @param configurationProvider Configuration provider.
     */
    public void selectNextNode(E environment,
                               ConfigurationProvider configurationProvider) {

        AntPolicy<C, E> selectNodePolicity = getAntPolicies(
                AntPolicyType.NODE_SELECTION, ONE_POLICY).get(0);

        // TODO(cgavidia): With this approach, the policy is a shared resource between ants. This doesn't allow parallelism.
        selectNodePolicity.setAnt(this);
        boolean policyResult = selectNodePolicity.applyPolicy(environment, configurationProvider);
        if (!policyResult) {
            throw new ConfigurationException("The node selection policy " + selectNodePolicity.getClass().getName() +
                    " wasn't able to select a node.");
        }

        List<AntPolicy<C, E>> afterNodeSelection = getAntPolicies(AntPolicyType.AFTER_NODE_SELECTION, DONT_CHECK_NUMBERS);
        afterNodeSelection.forEach((antPolicy) -> {
            antPolicy.setAnt(this);
            antPolicy.applyPolicy(environment, configurationProvider);
        });

    }

    /**
     * Improves the quality of the solution produced.
     *
     * @param environment           Environment where the ant is building a solution.
     * @param configurationProvider Configuration provider.
     */
    public void doAfterSolutionIsReady(E environment,
                                       ConfigurationProvider configurationProvider) {
        List<AntPolicy<C, E>> antPolicies = getAntPolicies(
                AntPolicyType.AFTER_SOLUTION_IS_READY, DONT_CHECK_NUMBERS);
        // TODO(cgavidia): With this approach, the policy is a shared resource between ants. This doesn't allow parallelism.
        antPolicies.forEach((antPolicy) -> {
            antPolicy.setAnt(this);
            antPolicy.applyPolicy(environment, configurationProvider);
        });

    }

    /**
     * Verifies if a component. is already included in the solution.
     *
     * @param component Component to verify.
     * @return True if the node is already visited. False otherwise.
     */

    public boolean isNodeVisited(C component) {
        boolean visited = false;

        Boolean inVisitedMap = visitedComponents.get(component);

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
     * Gets the current index for the Ant while constructing a solution.
     *
     * @return Current index.
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Returns true when the solution build by the current Ant is finished.
     *
     * @param environment Environment instance with problem information.
     * @return True if the solution is finished, false otherwise.
     */
    public abstract boolean isSolutionReady(E environment);

    /**
     * Calculates the cost associated to the solution build, which is needed to determine the performance of the Ant.
     *
     * @param environment Environment instance with problem information.
     * @return The cost of the solution built.
     */
    public abstract double getSolutionCost(E environment);

    /**
     * Calculates the heuristic contribution for the cost of the solution by adding a component at an specific position.
     *
     * @param solutionComponent  Solution component.
     * @param positionInSolution Position of this component in the solution.
     * @param environment        Environment instance with problem information.
     * @return Heurisitic contribution.
     */
    public abstract Double getHeuristicValue(C solutionComponent,
                                             Integer positionInSolution, E environment);

    /**
     * The components that are available for selection while an Ant is constructing its solution.
     *
     * @param environment Environment instance with problem information.
     * @return List of available components.
     */
    public abstract List<C> getNeighbourhood(E environment);


    // TODO(cgavidia): Maybe we should move this to Environment.

    /**
     * Returns the pheromone value associated to a solution component at an specific position
     *
     * @param solutionComponent  Solution component.
     * @param positionInSolution Position of this component in the solution.
     * @param environment        Environment instance with problem information.
     * @return Pheromone value.
     */
    public abstract Double getPheromoneTrailValue(C solutionComponent,
                                                  Integer positionInSolution, E environment);

    /**
     * Updates the value of a cell on the pheromone matrix.
     *
     * @param solutionComponent  Solution component.
     * @param positionInSolution Position of this component in the solution.
     * @param environment        Environment instance with problem information.
     * @param value              New pheromone value.
     */
    public abstract void setPheromoneTrailValue(C solutionComponent,
                                                Integer positionInSolution, E environment, Double value);


    public List<C> getSolution() {
        return solution;
    }

    public void setSolution(List<C> solution) {
        this.solution = solution;
    }

    public Map<C, Boolean> getVisited() {
        return visitedComponents;
    }

    public void setVisited(Map<C, Boolean> visited) {
        this.visitedComponents = visited;
    }


}
