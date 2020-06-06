package isula.aco.algorithms.iteratedants;

import isula.aco.Environment;
import isula.aco.PerformanceTracker;
import isula.aco.exception.InvalidInputException;
import isula.aco.test.BaseAntColonyTest;
import isula.aco.test.DummyFactory;
import org.junit.Test;

import javax.naming.ConfigurationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static junit.framework.Assert.*;

public class ConstructPartialSolutionTest extends BaseAntColonyTest {


    public ConstructPartialSolutionTest() throws InvalidInputException, ConfigurationException {
        super(DummyFactory.createDummyConfigurationProvider());
    }

    @Test
    public void testApplyPolicy() throws ConfigurationException {
        int componentsToRemove = 1;
        getAntColony().addAntPolicies(new ConstructPartialSolution<>() {
            @Override
            public int getNumberOfComponentsToRemove() {
                return componentsToRemove;
            }

            @Override
            public List<Integer> getNewPartialSolution(List<Integer> indexesForRemoval) {
                List<Integer> partialSolution = new ArrayList<>(getAnt().getSolution());
                indexesForRemoval.forEach((index) -> partialSolution.set(index, null));
                return partialSolution;
            }
        });

        getProblemSolver().solveProblem();
        AntWithPartialSolution<Integer, Environment> ant = (AntWithPartialSolution<Integer, Environment>) getAntColony().getHive().get(0);


        Collection<Integer> originalPartialSolution = ant.getPartialSolution();
        List<Integer> partialSolution = originalPartialSolution
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        assertNotNull(partialSolution);
        assertTrue(partialSolution.size() < SOLUTION_LENGTH);

        ant.clear();
        assertEquals(ant.getSolution(), ant.getPartialSolution());


    }
}