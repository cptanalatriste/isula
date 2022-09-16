package integration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PermutationGenerator {

    public static List<List<String>> getAllPermutations(List<String> items) {
        List<List<String>> allPermutations = new ArrayList<>();
        generatePermutations(items, allPermutations, items.size());
        return allPermutations;
    }

    private static void generatePermutations(List<String> items, List<List<String>> allPermutations,
                                             int maxIndex) {
        if (maxIndex <= 0) {
            allPermutations.add(items);
            return;
        }

        List<String> candidatePermutation = new ArrayList<>(items);

        int indexAtTheEnd = maxIndex - 1;
        for (int currentIndex = 0; currentIndex < maxIndex; currentIndex += 1) {
            Collections.swap(candidatePermutation, currentIndex, indexAtTheEnd);
            generatePermutations(candidatePermutation, allPermutations, indexAtTheEnd);
            Collections.swap(candidatePermutation, currentIndex, indexAtTheEnd);
        }
    }
}
