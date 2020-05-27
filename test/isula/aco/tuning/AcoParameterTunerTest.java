package isula.aco.tuning;

import isula.aco.ConfigurationProvider;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class AcoParameterTunerTest {


    @Test
    public void testGetOptimalConfiguration() {


        BasicConfigurationProvider targetConfiguration = new BasicConfigurationProvider();
        targetConfiguration.setNumberOfAnts(2);
        targetConfiguration.setEvaporationRatio(.1);
        targetConfiguration.setNumberOfIterations(3);
        targetConfiguration.setInitialPheromoneValue(20);
        targetConfiguration.setHeuristicImportance(2.);
        targetConfiguration.setPheromoneImportance(3.);

        List<Integer> numberOfAntsValues = Collections.singletonList(2);
        List<Double> evaporationRatioValues = Collections.singletonList(.1);
        List<Integer> numberOfIterationValues = Collections.singletonList(3);
        List<Double> initialPheromoneValues = Collections.singletonList(20.);
        List<Double> heuristicImportanceValues = Collections.singletonList(2.);
        List<Double> pheromoneImportanceValues = Arrays.asList(1., 3., 5.);

        AcoParameterTuner parameterTuner = new AcoParameterTuner(numberOfAntsValues, evaporationRatioValues,
                numberOfIterationValues, initialPheromoneValues, heuristicImportanceValues, pheromoneImportanceValues);


        ConfigurationProvider resultingConfiguration = parameterTuner.getOptimalConfiguration(configurationProvider -> {

            if (configurationProvider.getNumberOfAnts() == targetConfiguration.getNumberOfAnts() &&
                    configurationProvider.getEvaporationRatio() == targetConfiguration.getEvaporationRatio() &&
                    configurationProvider.getNumberOfIterations() == targetConfiguration.getNumberOfIterations() &&
                    configurationProvider.getInitialPheromoneValue() == targetConfiguration.getInitialPheromoneValue() &&
                    configurationProvider.getHeuristicImportance() == targetConfiguration.getHeuristicImportance() &&
                    configurationProvider.getPheromoneImportance() == targetConfiguration.getPheromoneImportance()

            ) {

                return 1.0;
            }
            return 10.0;
        });

        assertEquals(targetConfiguration, resultingConfiguration);
    }
}
