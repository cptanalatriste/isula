package isula.aco.algorithms.iteratedants;

import isula.aco.Ant;
import isula.aco.Environment;

import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

public abstract class AntWithPartialSolution<C, E extends Environment> extends Ant<C, E> {

    private static final Logger logger = Logger.getLogger(AntWithPartialSolution.class.getName());

    private Collection<C> partialSolution = Collections.emptyList();

    @Override
    public void clear() {
        super.clear();
        this.partialSolution
                .forEach(candidateIndex -> this.visitNode(candidateIndex, this.getEnvironment()));

        logger.fine("Partial solution loaded: " + this.getSolution());

    }

    public abstract E getEnvironment();

    protected Collection<C> getPartialSolution() {
        return this.partialSolution;
    }

    public void setPartialSolution(Collection<C> partialSolution) {
        this.partialSolution = partialSolution;
    }
}
