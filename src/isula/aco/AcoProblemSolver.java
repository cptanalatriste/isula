package isula.aco;

import javax.naming.ConfigurationException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main component of the framework: Is the one in charge of making a colony
 * an ants to traverse an environment in order to generate solutions.
 * <p>
 * <p>The solveProblem() method is the one that starts the optimization process.
 * Previously, you have to properly configure your solver by assigning it a
 * Colony, an Environment and Daemon Actions (if required).
 *
 * @param <C> Class for components of a solution.
 * @param <E> Class representing the Environment.
 * @author Carlos G. Gavidia
 */
public class AcoProblemSolver<C, E extends Environment> {

    private static Logger logger = Logger.getLogger(AcoProblemSolver.class
            .getName());

    private List<C> bestSolution;
    private double bestSolutionCost = 0.0;
    private double executionTime = 0.0;
    private String bestSolutionAsString = "";

    private E environment;
    private AntColony<C, E> antColony;

    // TODO(cgavidia): Maybe we should handle a list of configuration providers.
    private ConfigurationProvider configurationProvider;

    private List<DaemonAction<C, E>> daemonActions = new ArrayList<>();

    /**
     * Prepares the solver for problem resolution.
     *
     * @param environment Environment instance, with problem-related information.
     * @param colony      The Ant Colony with specialized ants.
     * @param config      Algorithm configuration.
     */
    public void initialize(E environment, AntColony<C, E> colony, ConfigurationProvider config)
            throws ConfigurationException {

        if (colony == null) {
            throw new ConfigurationException("The problem solver needs an instance of AntColony to be initialized");
        }
        colony.buildColony(environment);
        this.setAntColony(colony);

        this.setConfigurationProvider(config);
        this.setEnvironment(environment);
    }

    public void initialize(E environment, AntColony<C, E> colony, ConfigurationProvider config, Duration timeLimit)
            throws ConfigurationException {

        initialize(environment, colony, config);
        colony.setTimeLimit(timeLimit);
    }

    /**
     * Adds a list of Daemon Actions for the current solver. A daemon action is a global procedure applied
     * while algorithm execution.
     *
     * @param daemonActions Daemon actions.
     */
    @SafeVarargs
    public final void addDaemonActions(DaemonAction<C, E>... daemonActions) {
        for (DaemonAction<C, E> daemonAction : daemonActions) {
            this.addDaemonAction(daemonAction);
        }
    }

    /**
     * Adds a Daemon Action for the current solver.
     *
     * @param daemonAction Daemon action.
     */
    private void addDaemonAction(DaemonAction<C, E> daemonAction) {

        daemonAction.setAntColony(antColony);
        daemonAction.setEnvironment(environment);
        daemonAction.setProblemSolver(this);
        daemonActions.add(daemonAction);
    }

    /**
     * Solves an optimization problem using a Colony of Ants.
     *
     * @throws ConfigurationException If algorithm parameters aren't properly configured.
     */
    public void solveProblem() throws ConfigurationException {
        logger.info("Starting computation at: " + new Date());
        Instant executionStartTime = Instant.now();

        applyDaemonActions(DaemonActionType.INITIAL_CONFIGURATION);

        logger.info("STARTING ITERATIONS");
        int numberOfIterations = configurationProvider.getNumberOfIterations();

        if (numberOfIterations < 1) {
            throw new ConfigurationException(
                    "No iterations are programed for this solver. Check your Configuration Provider.");
        }

        logger.info("Number of iterations: " + numberOfIterations);

        int iteration = 0;

        while (iteration < numberOfIterations) {
            Instant iterationStart = Instant.now();

            antColony.clearAntSolutions();
            boolean terminateExecution = antColony.buildSolutions(environment, configurationProvider, executionStartTime);


            // TODO(cgavidia): This should reference the Update Pheromone routine. Maybe with the Policy hierarchy.
            applyDaemonActions(DaemonActionType.AFTER_ITERATION_CONSTRUCTION);

            Instant iterationEnd = Instant.now();
            long iterationTime = Duration.between(iterationStart, iterationEnd).getSeconds();
            evaluateIterationPerformance(iteration, iterationTime, environment);
            iteration++;

            if (terminateExecution) {
                break;
            }
        }

        logger.info("Finishing computation at: " + new Date());
        Instant executionEndTime = Instant.now();
        executionTime = Duration.between(executionStartTime, executionEndTime).getSeconds();
        logger.info("Duration (in seconds): " + executionTime);

        logger.info("EXECUTION FINISHED");
        logger.info("Best solution cost: " + bestSolutionCost);
        logger.info("Best solution:" + bestSolutionAsString);

    }

    /**
     * Updates the information of the best solution produced with the solutions
     * produced by the Colony.
     *
     * @param iterationTimeInSeconds Time spent during the iteration.
     * @param environment            Environment where the solutions where produced.
     */
    public void evaluateIterationPerformance(int iteration, long iterationTimeInSeconds, E environment) {
        logger.log(Level.FINE, "GETTING BEST SOLUTION FOUND");

        Ant<C, E> bestAnt = antColony.getBestPerformingAnt(environment);
        double bestIterationCost = bestAnt.getSolutionCost(environment);
        logger.fine("Iteration best cost: " + bestIterationCost);

        if (bestSolution == null
                || bestSolutionCost > bestIterationCost) {
            bestSolution = new ArrayList<>(bestAnt.getSolution());
            bestSolutionCost = bestIterationCost;
            bestSolutionAsString = bestAnt.getSolutionAsString();

            logger.fine("Best solution so far > Cost: " + bestSolutionCost
                    + ", Solution: " + bestSolutionAsString);

        }

        logger.info("Current iteration: " + iteration + " Iteration best: " + bestIterationCost + " Best solution cost: "
                + bestSolutionCost + " Iteration Duration (s): " + iterationTimeInSeconds);

    }

    /**
     * Applies all daemon actions of a specific type.
     *
     * @param daemonActionType Daemon action type.
     */
    private void applyDaemonActions(DaemonActionType daemonActionType) {
        for (DaemonAction<C, E> daemonAction : daemonActions) {
            if (daemonActionType.equals(daemonAction.getAcoPhase())) {
                daemonAction.applyDaemonAction(this.getConfigurationProvider());
            }
        }
    }

    public E getEnvironment() {
        return environment;
    }

    public void setEnvironment(E environment) {
        this.environment = environment;
    }

    public AntColony<C, E> getAntColony() {
        return antColony;
    }

    public void setAntColony(AntColony<C, E> antColony) {
        this.antColony = antColony;
    }

    public ConfigurationProvider getConfigurationProvider() {
        if (this.configurationProvider == null) {
            throw new isula.aco.exception.ConfigurationException(
                    "No Configuration Provider was associated with this solver");
        }

        return configurationProvider;
    }

    public void setConfigurationProvider(
            ConfigurationProvider configurationProvider) {
        this.configurationProvider = configurationProvider;
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

    public void setBestSolutionCost(double bestSolutionCost) {
        this.bestSolutionCost = bestSolutionCost;
    }

    @Override
    public String toString() {
        return "AcoProblemSolver{" +
                "bestSolutionCost=" + bestSolutionCost +
                ", executionTime=" + executionTime +
                ", bestSolutionAsString='" + bestSolutionAsString + '\'' +
                ", environment=" + environment +
                ", antColony=" + antColony +
                ", configurationProvider=" + configurationProvider +
                ", daemonActions=" + daemonActions +
                '}';
    }
}
