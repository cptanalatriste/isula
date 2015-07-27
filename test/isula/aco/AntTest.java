package isula.aco;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import isula.aco.algorithms.antsystem.OnlinePheromoneUpdate;
import isula.aco.exception.InvalidInputException;

import org.junit.Before;
import org.junit.Test;

import java.util.List;


public class AntTest {

  private static final int SOLUTION_SIZE = 3;
  private static final double[][] SAMPLE_PROBLEM_GRAPH = new double[3][3];

  private Ant<Integer, Environment> dummyAnt;
  private OnlinePheromoneUpdate<Integer, Environment> antPolicy = 
      new OnlinePheromoneUpdate<Integer, Environment>() {
    @Override
    protected double getNewPheromoneValue(Integer solutionComponent,
        Environment environment, ConfigurationProvider configurationProvider) {
      return 0;
    }
  };

  /**
   * Configures a Dummy Ant for testing.
   * 
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    this.dummyAnt = new Ant<Integer, Environment>() {

      @Override
      public List<Integer> getNeighbourhood(Environment environment) {
        return null;
      }

      @Override
      public Double getPheromoneTrailValue(Integer solutionComponent,
          Integer positionInSolution, Environment environment) {
        return null;
      }

      @Override
      public Double getHeuristicValue(Integer solutionComponent,
          Integer positionInSolution, Environment environment) {
        return null;
      }

      @Override
      public void setPheromoneTrailValue(Integer solutionComponent,
          Environment environment, Double value) {
      }

      @Override
      public double getSolutionQuality(Environment environment) {
        return 0;
      }

      @Override
      public boolean isSolutionReady(Environment environment) {
        return false;
      }

    };

    this.dummyAnt.setSolution(new Integer[SOLUTION_SIZE]);

    this.dummyAnt.visitNode(0);
    this.dummyAnt.visitNode(1);
    this.dummyAnt.visitNode(2);
  }

  @Test
  public void testVisitNode() {

    assertEquals(3, this.dummyAnt.getCurrentIndex());
    assertTrue(this.dummyAnt.isNodeVisited(0));
    assertTrue(this.dummyAnt.isNodeVisited(1));
    assertTrue(this.dummyAnt.isNodeVisited(2));
    assertFalse(this.dummyAnt.isNodeVisited(3));
  }

  @Test
  public void testClear() {
    this.dummyAnt.clear();

    assertEquals(0, this.dummyAnt.getCurrentIndex());
    assertTrue(this.dummyAnt.getVisited().isEmpty());

    Integer[] solution = this.dummyAnt.getSolution();

    for (Integer component : solution) {
      assertNull(component);
    }
  }

  @Test
  public void testGetSolutionAsString() {
    String expectedString = " 0 1 2";

    assertEquals(expectedString, this.dummyAnt.getSolutionAsString());
  }

  @Test
  public void testAddPolicy() {

    this.dummyAnt.addPolicy(antPolicy);

    assertEquals(antPolicy,
        this.dummyAnt.getAntPolicy(antPolicy.getPolicyType(), 1));
    assertNull(this.dummyAnt.getAntPolicy(AntPolicyType.NODE_SELECTION, -1));
  }

  @Test
  public void testDoAfterSolutionIsReady() throws InvalidInputException {
    Environment dummyEnvironment = new Environment(SAMPLE_PROBLEM_GRAPH) {

      @Override
      protected double[][] createPheromoneMatrix() {
        return new double[3][3];
      }

    };
    ConfigurationProvider configurationProvider = new ConfigurationProvider() {

      public int getNumberOfIterations() {
        return 0;
      }

      public int getNumberOfAnts() {
        return 0;
      }

      public double getInitialPheromoneValue() {
        return 0;
      }

      public double getEvaporationRatio() {
        return 0;
      }
    };

    this.dummyAnt.addPolicy(antPolicy);
    this.dummyAnt.doAfterSolutionIsReady(dummyEnvironment,
        configurationProvider);

    assertEquals(this.dummyAnt, antPolicy.getAnt());
  }

}
