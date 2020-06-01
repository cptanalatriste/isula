package isula.aco;

import javax.naming.ConfigurationException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParallelAcoProblemSolver<C, E extends Environment> extends AcoProblemSolver<C, E> {

    private static Logger logger = Logger.getLogger(ParallelAcoProblemSolver.class
            .getName());

    private List<AntColony<C, E>> antColonies;
    private List<E> environments;
    private int parallelRuns;

    @Override
    public void solveProblem() {

        logger.info("Starting computation at: " + new Date());
        Instant executionStartTime = Instant.now();

        List<PerformanceTracker<C, E>> performancePerColony = IntStream.range(0, this.parallelRuns)
                .parallel()
                .mapToObj((runIndex) -> {
                    try {
                        AntColony<C, E> antColony = this.antColonies.get(runIndex);
                        E environment = this.environments.get(runIndex);

                        return kickOffColony(antColony, environment, executionStartTime);
                    } catch (ConfigurationException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());

        PerformanceTracker<C, E> bestPerformingTracker = performancePerColony
                .stream()
                .min(Comparator.comparing(PerformanceTracker::getBestSolutionCost))
                .orElseThrow(NoSuchElementException::new);

        this.updateGlobalMetrics(executionStartTime, bestPerformingTracker);

    }

    public void initialize(Supplier<E> environmentProvider, Function<ConfigurationProvider, AntColony<C, E>> colonySupplier, ConfigurationProvider config,
                           Duration timeLimit, int parallelRuns)
            throws ConfigurationException {

        this.setAntColony(null);
        this.setEnvironment(null);

        this.parallelRuns = parallelRuns;
        this.setConfigurationProvider(config);

        if (colonySupplier == null) {
            throw new ConfigurationException("The problem solver needs an instance of AntColony to be initialized");
        }

        antColonies = new ArrayList<>();
        environments = new ArrayList<>();

        IntStream.range(0, parallelRuns).forEachOrdered((run) -> {
            AntColony<C, E> antColony = colonySupplier.apply(config);
            E environment = environmentProvider.get();
            this.configureAntColony(antColony, environment, timeLimit);

            antColonies.add(antColony);
            environments.add(environment);
        });

    }


    public List<AntColony<C, E>> getAntColonies() {
        return antColonies;
    }

    public void addDaemonAction(Supplier<DaemonAction<C, E>> daemonActionSupplier) {

        IntStream.range(0, this.parallelRuns).forEachOrdered((colonyIndex) -> {
            AntColony<C, E> antColony = this.antColonies.get(colonyIndex);
            E environment = this.environments.get(colonyIndex);

            DaemonAction<C, E> daemonAction = daemonActionSupplier.get();
            configureDaemonAction(antColony, daemonAction, environment);
            getDaemonActions().add(daemonAction);
        });

    }
}
