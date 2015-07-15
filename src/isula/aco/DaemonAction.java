package isula.aco;

/**
 * Global actions that have impact in all the colony and its environment. As
 * such, instances have access to the Ant Colony instance and also at the
 * Environment.
 * 
 * <p>
 * The moment in the process where this actions take place is defined by the
 * Daemon Action type.
 * 
 * @author Carlos G. Gavidia
 * 
 * @param <C>
 *          Class for components of a solution.
 */
public abstract class DaemonAction<C, E extends Environment> {

  private DaemonActionType acoPhase;
  private E environment;
  private AntColony<C, E> antColony;

  public DaemonAction(DaemonActionType acoPhase) {
    super();
    this.acoPhase = acoPhase;
  }

  public DaemonActionType getAcoPhase() {
    return acoPhase;
  }

  public E getEnvironment() {
    return environment;
  }

  public void setEnvironment(E environment) {
    this.environment = environment;
  }

  public AntColony<C, E> getAntColony() {
    return antColony;
  }

  public void setAntColony(AntColony<C, E> antColony) {
    this.antColony = antColony;
  }

  public abstract void applyDaemonAction(
      ConfigurationProvider configurationProvider);
}
