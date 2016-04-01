package isula.aco;

/**
 * Classes that implement this type provide configuration information to the
 * Problem Solvers. This interface contain parameters used by almost all ACO
 * algorithms.
 *
 * @author Carlos G. Gavidia
 */
public interface ConfigurationProvider {

    /**
     * Number of ants used in the algorithm.
     *
     * @return Number of ants.
     */
    int getNumberOfAnts();

    /**
     * Pheromone decay factor.
     *
     * @return Pheromone decay factor.
     */
    double getEvaporationRatio();

    /**
     * Maximum number of iterations.
     *
     * @return Number of iterations.
     */
    int getNumberOfIterations();

    /**
     * Initial value of every cell on the Pheromone Matrix.
     *
     * @return Initial pheromone value.
     */
    double getInitialPheromoneValue();

    /**
     * Heuristic coefficient, controls the amount of contribution for heuristic information.
     *
     * @return Heuristic coefficient.
     */
    double getHeuristicImportance();

    /**
     * History coefficient, controls the amount of contribution of history expressed as
     * pheromone accumulation.
     *
     * @return History coefficient.
     */
    double getPheromoneImportance();

}
