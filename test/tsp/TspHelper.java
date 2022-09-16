package tsp;

import java.util.List;
import java.util.Map;

public class TspHelper {

    public static Integer calculateDistance(List<String> route, Map<String, Map<String, Integer>> distanceMap) {

        int totalDistance = 0;
        for (int currentIndex = 0; currentIndex < route.size() - 1; currentIndex += 1) {
            int nextIndex = currentIndex + 1;
            String currentCity = route.get(currentIndex);
            String nextCity = route.get(nextIndex);

            int currentDistance = distanceMap.get(currentCity).get(nextCity);
            totalDistance += currentDistance;
        }

        String routeEnd = route.get(route.size() - 1);
        String routeStart = route.get(0);
        int backHomeDistance = distanceMap.get(routeEnd).get(routeStart);

        return totalDistance + backHomeDistance;
    }

    public static Map<String, Map<String, Integer>> getSampleProblem() {
        return Map.of(
                "Buenos Aires", Map.of("Caracas", 5114,
                        "Denver", 9555,
                        "Edmonton", 11139,
                        "Houston", 8150),
                "Caracas", Map.of("Buenos Aires", 5114,
                        "Denver", 4996,
                        "Edmonton", 6295,
                        "Houston", 3633),
                "Denver", Map.of("Buenos Aires", 9555,
                        "Caracas", 4996,
                        "Edmonton", 1642,
                        "Houston", 1408),
                "Edmonton", Map.of("Buenos Aires", 11139,
                        "Caracas", 6295,
                        "Denver", 1642,
                        "Houston", 3009),
                "Houston", Map.of("Buenos Aires", 8150,
                        "Caracas", 3633,
                        "Denver", 1408,
                        "Edmonton", 3009));
    }

}