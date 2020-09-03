package isula.aco.simpletsp;

import isula.aco.Ant;
import isula.aco.AntColony;

public class SimpleTSPColony extends AntColony<Integer, SimpleTSPEnvironment> {
    /**
     * Creates a colony of ants
     *
     * @param numberOfAnts Number of ants in the colony.
     */
    public SimpleTSPColony(int numberOfAnts) {
        super(numberOfAnts);
    }

    @Override
    protected Ant<Integer, SimpleTSPEnvironment> createAnt(SimpleTSPEnvironment environment) {
        return new SimpleTSPAnt();
    }
}
