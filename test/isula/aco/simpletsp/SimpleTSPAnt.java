package isula.aco.simpletsp;

import isula.aco.Ant;
import isula.aco.simpletsp.SimpleTSPEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleTSPAnt extends Ant<Integer, SimpleTSPEnvironment> {

    public SimpleTSPAnt() {
        this.setSolution(new ArrayList<>());
    }

    @Override
    public boolean isSolutionReady(SimpleTSPEnvironment environment) {
        return false;
    }

    @Override
    public double getSolutionCost(SimpleTSPEnvironment environment, List<Integer> solution) {

        double solutionCost = 0.0;

        int[][] problemRepresentation = environment.getProblemRepresentation();
        for (int solutionIndex = 1; solutionIndex < solution.size(); solutionIndex += 1) {
            int edgeStart = solution.get(solutionIndex - 1);
            int edgeEnd = solution.get(solutionIndex);

            int edgeCost = problemRepresentation[edgeStart][edgeEnd];
            solutionCost += edgeCost;
        }

        solutionCost += problemRepresentation[solution.get(solution.size() - 1)][solution.get(0)];
        return solutionCost;
    }

    @Override
    public Double getHeuristicValue(Integer solutionComponent, Integer positionInSolution,
                                    SimpleTSPEnvironment environment) {
        int[][] problemRepresentation = environment.getProblemRepresentation();

        int lastVisitedCity = this.getSolution().get(positionInSolution - 1);
        int distance = problemRepresentation[lastVisitedCity][solutionComponent];
        return 1.0 / distance;
    }


    @Override
    public List<Integer> getNeighbourhood(SimpleTSPEnvironment environment) {
        return IntStream.range(0, environment.getProblemRepresentation().length)
                .boxed()
                .collect(Collectors.toList());
    }

    @Override
    public Double getPheromoneTrailValue(Integer solutionComponent, Integer positionInSolution,
                                         SimpleTSPEnvironment environment) {

        if (positionInSolution > 0) {
            int previousComponent = this.getSolution().get(positionInSolution - 1);
            return environment.getPheromoneMatrix()[previousComponent][solutionComponent];
        }

        return 0.0;

    }


    @Override
    public void setPheromoneTrailValue(Integer solutionComponent, Integer positionInSolution,
                                       SimpleTSPEnvironment environment, Double value) {

        if (positionInSolution > 0) {
            int previousComponent = this.getSolution().get(positionInSolution - 1);
            environment.getPheromoneMatrix()[previousComponent][solutionComponent] = value;
        }

    }
}
