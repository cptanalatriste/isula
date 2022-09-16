package tsp;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AntForTsp extends isula.aco.Ant<String, TspEnvironment> {

    private final List<String> citiesToVisit;
    private final Random random = new Random();

    public AntForTsp(List<String> citiesToVisit) {
        super();
        this.citiesToVisit = citiesToVisit;
        this.setSolution(new ArrayList<>());
    }

    @Override
    public void clear() {
        super.clear();

        int selectedIndex = random.nextInt(this.citiesToVisit.size());
        String initialCity = citiesToVisit.get(selectedIndex);
        this.visitNode(initialCity, null);
    }

    @Override
    public boolean isSolutionReady(TspEnvironment tspEnvironment) {
        return getCurrentIndex() == this.citiesToVisit.size();
    }

    @Override
    public double getSolutionCost(TspEnvironment tspEnvironment, List<String> solution) {
        return TspHelper.calculateDistance(solution, tspEnvironment.getDistanceMap());
    }

    @Override
    public Double getHeuristicValue(String candidateCity, Integer positionInSolution, TspEnvironment tspEnvironment) {

        String lastCity = this.getSolution().get(getCurrentIndex() - 1);
        Integer distance = tspEnvironment.getDistanceMap().get(lastCity).get(candidateCity);
        return 1 / distance.doubleValue();
    }

    @Override
    public List<String> getNeighbourhood(TspEnvironment tspEnvironment) {

        return this.citiesToVisit.stream()
                .filter(city -> !this.isNodeVisited(city))
                .collect(Collectors.toList());
    }

    @Override
    public Double getPheromoneTrailValue(String city, Integer cityIndex, TspEnvironment environment) {


        String lastCity = this.getSolution().get(getCurrentIndex() - 1);
        return environment.getPheromoneTrailValue(lastCity, city);
    }

    @Override
    public void setPheromoneTrailValue(String city, Integer cityIndex, TspEnvironment environment,
                                       Double pheromoneValue) {

        String lastCity = this.getSolution().get(getCurrentIndex() - 1);
        environment.setPheromoneTrailValue(lastCity, city, pheromoneValue);
    }
}
