package tsp;


import isula.aco.Ant;

public class AntColonyForTsp extends isula.aco.AntColony<String, TspEnvironment> {


    public AntColonyForTsp(int numberOfAnts) {
        super(numberOfAnts);
    }

    @Override
    protected Ant<String, TspEnvironment> createAnt(TspEnvironment environment) {
        return new AntForTsp(environment.getAllCities());
    }
}
