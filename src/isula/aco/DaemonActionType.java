package isula.aco;

/**
 * Types of Daemon Actions. Currently, only two are supported:
 * 
 * <ul>
 * <li>Before starting constructing solutions.
 * <li>After the end of a construction iteration. </lu>
 * 
 * @author Carlos G. Gavidia
 * 
 */
public enum DaemonActionType {
  INITIAL_CONFIGURATION, AFTER_ITERATION_CONSTRUCTION
}
