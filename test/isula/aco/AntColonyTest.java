package isula.aco;

import static org.junit.Assert.assertEquals;

import isula.aco.test.DummyFactory;

import org.junit.Before;
import org.junit.Test;

import java.util.List;



public class AntColonyTest {

  private static final int SOLUTION_LENGTH = 3;
  private static final int COMMON_COST = 2;
  private static final int NUMBER_OF_ANTS = 3;
  private static final int BEST_COST = 1;
  protected static final Integer SAMPLE_NODE = 10;
  private AntColony<Integer, Environment> dummyColony;
  private Environment environment;

  /**
   * Prepares a dummy colony for testing.
   * 
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    this.dummyColony = new AntColony<Integer, Environment>(NUMBER_OF_ANTS) {

      @Override
      protected Ant<Integer, Environment> createAnt(Environment environment) {
        return DummyFactory.createDummyAnt(COMMON_COST, SOLUTION_LENGTH);
      }
    };

    double[][] problemGraph = new double[3][4];
    int pheromoneRows = 3;
    int pheromoneColumns = 4;
    this.environment = DummyFactory.createDummyEnvironment(problemGraph,
        pheromoneRows, pheromoneColumns);

  }

  @Test
  public void testAntColony() {
    assertEquals(NUMBER_OF_ANTS, this.dummyColony.getNumberOfAnts());
  }

  @Test
  public void testBuildColony() {
    this.dummyColony.buildColony(environment);
    assertEquals(NUMBER_OF_ANTS, this.dummyColony.getHive().size());
  }

  @Test
  public void testGetBestPerformingAnt() {
    Ant<Integer, Environment> theBestAnt = DummyFactory.createDummyAnt(
        BEST_COST, SOLUTION_LENGTH);

    this.dummyColony.buildColony(environment);
    this.dummyColony.getHive().add(theBestAnt);

    assertEquals(BEST_COST,
        this.dummyColony.getBestPerformingAnt(this.environment)
            .getSolutionCost(this.environment), 0.001);
  }

  @Test
  public void testClearAntSolutions() {
    List<Ant<Integer, Environment>> hive = this.dummyColony.getHive();

    for (Ant<Integer, Environment> ant : hive) {
      ant.visitNode(1);
    }

    this.dummyColony.clearAntSolutions();

    for (Ant<Integer, Environment> ant : hive) {
      assertEquals(0, ant.getCurrentIndex());
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testBuildSolutions() {

    AntPolicy<Integer, Environment> nodeSelectionPolicy = new AntPolicy<Integer, Environment>(
        AntPolicyType.NODE_SELECTION) {

      @Override
      public void applyPolicy(Environment environment,
          ConfigurationProvider configurationProvider) {
        this.getAnt().visitNode(SAMPLE_NODE);
      }
    };

    this.dummyColony.buildColony(this.environment);
    this.dummyColony.addAntPolicies(nodeSelectionPolicy);

    this.dummyColony.buildSolutions(this.environment,
        DummyFactory.createDummyConfigurationProvider());

    for (Ant<Integer, Environment> ant : this.dummyColony.getHive()) {
      assertEquals(SOLUTION_LENGTH, ant.getSolution().length);

      for (Integer component : ant.getSolution()) {
        assertEquals(SAMPLE_NODE, component);
      }
    }
  }
}
