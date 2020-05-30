package isula.aco.test;

import isula.aco.*;
import isula.aco.algorithms.antsystem.StartPheromoneMatrix;
import isula.aco.exception.InvalidInputException;

import javax.naming.ConfigurationException;

public class BaseAntColonyTest {

    protected static final int SOLUTION_LENGTH = 3;
    protected static final int SOLUTION_COST = 2;
    protected static final Integer COMPONENT_FROM_POLICY = 0;

    private final AcoProblemSolver<Integer, Environment> problemSolver;
    private final AntColony<Integer, Environment> antColony;

    private ConfigurationProvider configurationProvider;
    private Environment environment;

    public BaseAntColonyTest(ConfigurationProvider configurationProvider) throws
            InvalidInputException, ConfigurationException {

        this.configurationProvider = configurationProvider;
        AntPolicy<Integer, Environment> nodeSelectionPolicy = new AntPolicy<Integer, Environment>(
                AntPolicyType.NODE_SELECTION) {

            @Override
            public boolean applyPolicy(Environment environment,
                                       ConfigurationProvider configurationProvider) {
                this.getAnt().visitNode(COMPONENT_FROM_POLICY, environment);
                return true;
            }
        };


        double[][] problemGraph = new double[3][4];
        int pheromoneRows = 3;
        int pheromoneColumns = 4;
        environment = DummyFactory.createDummyEnvironment(problemGraph,
                pheromoneRows, pheromoneColumns);
        StartPheromoneMatrix<Integer, Environment> startPheromoneMatrix = new StartPheromoneMatrix<>();
        startPheromoneMatrix.setEnvironment(environment);
        startPheromoneMatrix.applyDaemonAction(configurationProvider);

        problemSolver = new AcoProblemSolver<>();
        antColony = new AntColony<Integer, Environment>(1) {
            @Override
            protected Ant<Integer, Environment> createAnt(Environment environment) {
                return DummyFactory.createDummyAnt(SOLUTION_COST, SOLUTION_LENGTH);
            }
        };
        problemSolver.initialize(environment, antColony, configurationProvider);
        antColony.addAntPolicies(nodeSelectionPolicy);
    }

    public ConfigurationProvider getConfigurationProvider() {
        return configurationProvider;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public AcoProblemSolver<Integer, Environment> getProblemSolver() {
        return problemSolver;
    }

    public AntColony<Integer, Environment> getAntColony() {
        return antColony;
    }
}
