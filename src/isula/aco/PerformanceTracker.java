package isula.aco;

import isula.aco.exception.SolutionConstructionException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PerformanceTracker<C, E extends Environment> {

    private static Logger logger = Logger.getLogger(PerformanceTracker.class
            .getName());

    private List<C> bestSolution;
    private double bestSolutionCost;
    private String bestSolutionAsString;

    private long generatedSolutions;

    /**
     * Updates the information of the best solution produced with the solutions
     * produced by the Colony.
     *
     * @param iterationTimeInSeconds Time spent during the iteration.
     * @param environment            Environment where the solutions where produced.
     */
    public void updateIterationPerformance(AntColony<C, E> antColony, int iteration, long iterationTimeInSeconds,
                                           E environment) {
        logger.log(Level.FINE, "GETTING BEST SOLUTION FOUND");


        long iterationSolutions = antColony.getHive()
                .stream()
                .filter((ant) -> ant.isSolutionReady(environment))
                .count();

        this.generatedSolutions += iterationSolutions;

        Ant<C, E> bestAnt = antColony.getBestPerformingAnt(environment);

        if (!this.isStateValid(bestAnt, environment)) {
            throw new SolutionConstructionException("Performance Tracker is in an inconsistent state. Solution:"
                    + this.bestSolution + " Solution Cost: " + bestSolutionCost + " String representation: " +
                    bestSolutionAsString);
        }

        double bestIterationCost = bestAnt.getSolutionCost(environment);
        logger.fine("Iteration best cost: " + bestIterationCost);

        if (bestSolution == null
                || bestSolutionCost > bestIterationCost) {
            bestSolution = bestAnt.getSolution().stream().collect(Collectors.toUnmodifiableList());
            bestSolutionCost = bestIterationCost;
            bestSolutionAsString = bestAnt.getSolutionAsString();

            logger.fine("Best solution so far > Cost: " + bestSolutionCost
                    + ", Solution as string: " + bestSolutionAsString + " Stored solution: " + bestSolution);

        }

        logger.info("Current iteration: " + iteration + " Iteration solutions: " + iterationSolutions +
                " Iteration best: " + bestIterationCost + " Iteration Duration (s): " + iterationTimeInSeconds);

        logger.info(" Global solution cost: " + bestSolutionCost);
        logger.fine(" Stored solution: " + bestSolution + " Solution as String: " + bestSolutionAsString);
    }

    private boolean isStateValid(Ant<C, E> ant, E environment) {

        if (this.bestSolution == null && this.bestSolutionAsString == null && this.bestSolutionCost == 0.0) {
            return true;
        }

        double expectedSolutionCost = ant.getSolutionCost(environment, bestSolution);
        String expectedSolutionAsString = ant.getSolutionAsString(bestSolution);

        if (Math.abs(expectedSolutionCost - bestSolutionCost) <= 0.001 &&
                expectedSolutionAsString.equals(bestSolutionAsString)) {
            return true;
        }

        return false;
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

    public long getGeneratedSolutions() {
        return generatedSolutions;
    }

    public void setGeneratedSolutions(Long generatedSolutions) {
        this.generatedSolutions = generatedSolutions;
    }
}
