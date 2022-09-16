package integration;


import tsp.TspHelper;

import java.util.*;

public class BruteForceTspSolver {

    private final Map<Integer, List<List<String>>> routesPerCost = new HashMap<>();

    public List<List<String>> findOptimalRoutes(Map<String, Map<String, Integer>> distanceMap) {

        List<String> cities = new ArrayList<>(distanceMap.keySet());
        //Generating all possible permutations.
        List<List<String>> allPermutations =
                PermutationGenerator.getAllPermutations(cities);

        for (List<String> candidateRoute : allPermutations) {
            //Per permutation, we calculate the total distance.
            int currentRouteDistance =
                    TspHelper.calculateDistance(candidateRoute, distanceMap);
            if (routesPerCost.containsKey(currentRouteDistance)) {
                routesPerCost.get(currentRouteDistance).add(candidateRoute);
            } else {
                List<List<String>> routeList = new ArrayList<>();
                routeList.add(candidateRoute);
                routesPerCost.put(currentRouteDistance, routeList);
            }
        }

        Integer minimumDistance = Collections.min(routesPerCost.keySet());

        return routesPerCost.get(minimumDistance);
    }

}
