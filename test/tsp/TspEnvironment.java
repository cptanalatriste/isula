package tsp;

import isula.aco.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class TspEnvironment extends Environment {

    private final Map<String, Map<String, Integer>> distanceMap;
    private final List<String> allCities;
    private final Map<String, Integer> cityToIndex = new HashMap<>();

    public TspEnvironment(Map<String, Map<String, Integer>> distanceMap) {
        this.distanceMap = distanceMap;
        this.allCities = new ArrayList<>(this.distanceMap.keySet());
        IntStream.range(0, allCities.size())
                .forEach(index -> cityToIndex.put(allCities.get(index), index));

        this.setPheromoneMatrix(createPheromoneMatrix());
    }

    public List<String> getAllCities() {
        return this.allCities;
    }

    @Override
    protected double[][] createPheromoneMatrix() {
        int cities = 0;
        if (this.allCities != null) {
            cities = this.allCities.size();
        }
        return new double[cities][cities];
    }

    public Map<String, Map<String, Integer>> getDistanceMap() {
        return distanceMap;
    }

    public Double getPheromoneTrailValue(String lastCity, String city) {
        double[][] pheromoneMatrix = this.getPheromoneMatrix();

        return pheromoneMatrix[this.cityToIndex.get(lastCity)][this.cityToIndex.get(city)];
    }

    public void setPheromoneTrailValue(String lastCity, String city, Double pheromoneValue) {
        double[][] pheromoneMatrix = this.getPheromoneMatrix();
        pheromoneMatrix[this.cityToIndex.get(lastCity)][this.cityToIndex.get(city)] = pheromoneValue;
        pheromoneMatrix[this.cityToIndex.get(city)][this.cityToIndex.get(lastCity)] = pheromoneValue;

    }


}
