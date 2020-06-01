package isula.aco;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PerformanceTracker<C, E extends Environment> {

    private static Logger logger = Logger.getLogger(PerformanceTracker.class
            .getName());

    private List<C> bestSolution;
    private double bestSolutionCost;
    private String bestSolutionAsString;

    /**
     * Updates the information of the best solution produced with the solutions
     * produced by the Colony.
     *
     * @param iterationTimeInSeconds Time spent during the iteration.
     * @param environment            Environment where the solutions where produced.
     */
    public void updateIterationPerformance(Ant<C, E> bestAnt, int iteration, long iterationTimeInSeconds, E environment) {
        logger.log(Level.FINE, "GETTING BEST SOLUTION FOUND");

        double bestIterationCost = bestAnt.getSolutionCost(environment);
        logger.fine("Iteration best cost: " + bestIterationCost);

        if (bestSolution == null
                || bestSolutionCost > bestIterationCost) {
            bestSolution = Collections.unmodifiableList(bestAnt.getSolution());
            bestSolutionCost = bestIterationCost;
            bestSolutionAsString = bestAnt.getSolutionAsString();

            logger.fine("Best solution so far > Cost: " + bestSolutionCost
                    + ", Solution: " + bestSolutionAsString);

        }

        logger.info("Current iteration: " + iteration + " Iteration best: " + bestIterationCost + " Best solution cost: "
                + bestSolutionCost + " Iteration Duration (s): " + iterationTimeInSeconds);

    }


    public List<C> getBestSolution() {
        return bestSolution;
    }

    public double getBestSolutionCost() {
        return bestSolutionCost;
    }

    public String getBestSolutionAsString() {
        return bestSolutionAsString;
    }
}
