package smalltsp;

import isula.aco.Ant;
import isula.aco.AntColony;

public class SmallTspAntColony extends AntColony<Integer, SmallTspEnvironment> {
    /**
     * Creates a colony of ants
     *
     * @param numberOfAnts Number of ants in the colony.
     */
    public SmallTspAntColony(int numberOfAnts) {
        super(numberOfAnts);
    }

    @Override
    protected Ant<Integer, SmallTspEnvironment> createAnt(SmallTspEnvironment environment) {
        return new SmallTspAnt();
    }
}
