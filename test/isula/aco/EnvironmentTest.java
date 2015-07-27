package isula.aco;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class EnvironmentTest {

  private static final double DELTA = 0.001;
  private static final int MATRIX_COLUMNS = 4;
  private static final int MATRIX_ROWS = 3;
  private static final double SAMPLE_PHEROMONE = 0.5;
  private Environment dummyEnvironment;

  /**
   * We're creating a dummy Environment class, with a pheromone matrix with the
   * dimensions specified in the constants.
   * 
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    double[][] problemGraph = new double[MATRIX_ROWS][MATRIX_COLUMNS];

    this.dummyEnvironment = DummyFactory.createDummyEnvironment(problemGraph,
        MATRIX_ROWS, MATRIX_COLUMNS);
  }

  @Test
  public void testEnvironment() {
    assertNotNull(this.dummyEnvironment);
    assertNotNull(this.dummyEnvironment.getProblemGraph());
    assertNotNull(this.dummyEnvironment.getPheromoneMatrix());
  }

  @Test
  public void testIsProblemGraphValid() {
    assertTrue(this.dummyEnvironment.isProblemGraphValid());
  }

  @Test
  public void testGetProblemGraph() {
    double[][] problemGraph = this.dummyEnvironment.getProblemGraph();
    assertEquals(MATRIX_ROWS, problemGraph.length);
    assertEquals(MATRIX_COLUMNS, problemGraph[0].length);
  }

  @Test
  public void testPheromoneMatrixConfiguration() {
    double[][] newPheromoneMatrix = new double[MATRIX_ROWS + 1][MATRIX_COLUMNS + 1];
    this.dummyEnvironment.setPheromoneMatrix(newPheromoneMatrix);

    assertEquals(MATRIX_ROWS + 1,
        this.dummyEnvironment.getPheromoneMatrix().length);
    assertEquals(MATRIX_COLUMNS + 1,
        this.dummyEnvironment.getPheromoneMatrix()[0].length);

  }

  @Test
  public void testPopulatePheromoneMatrix() {
    double[][] pheromoneMatrix = this.dummyEnvironment.getPheromoneMatrix();
    this.dummyEnvironment.populatePheromoneMatrix(SAMPLE_PHEROMONE);

    for (int i = 0; i < pheromoneMatrix.length; i++) {
      for (int j = 0; j < pheromoneMatrix[0].length; j++) {
        assertEquals(SAMPLE_PHEROMONE,
            this.dummyEnvironment.getPheromoneMatrix()[i][j], DELTA);
      }
    }
  }

  @Test
  public void testToString() {
    String environmentAsString = this.dummyEnvironment.toString();
    String expectedEnvironment = "Problem Graph: Rows " + MATRIX_ROWS
        + " Columns " + MATRIX_COLUMNS + "\n" + "Pheromone Matrix: Rows "
        + MATRIX_ROWS + " Columns " + MATRIX_COLUMNS;

    assertEquals(expectedEnvironment, environmentAsString);
  }

  @Test
  public void testApplyFactorToPheromoneMatrix() {
    double[][] pheromoneMatrix = this.dummyEnvironment.getPheromoneMatrix();

    this.dummyEnvironment.populatePheromoneMatrix(SAMPLE_PHEROMONE);
    this.dummyEnvironment.applyFactorToPheromoneMatrix(2);

    for (int i = 0; i < pheromoneMatrix.length; i++) {
      for (int j = 0; j < pheromoneMatrix[0].length; j++) {
        assertEquals(2 * SAMPLE_PHEROMONE,
            this.dummyEnvironment.getPheromoneMatrix()[i][j], DELTA);
      }
    }
  }
}
