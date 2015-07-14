package isula.aco;

/**
 * A type of Ant Policy. So far, only two types are supported:
 * 
 * <ul>
 * <li>A node selection policy that directs the process of adding solution
 * components
 * <li>An after solution is ready policy, to perform actions after the ant has
 * finished constructing a solution.
 * </ul>
 * 
 * @author Carlos G. Gavidia
 * 
 */
public enum AntPolicyType {
  NODE_SELECTION, AFTER_SOLUTION_IS_READY;
}
