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
 * @param <E>
 *          Class for components of a solution.
 */
public abstract class DaemonAction<E> {

  private DaemonActionType acoPhase;
  private Environment environment;
  private AntColony<E> antColony;

  public DaemonAction(DaemonActionType acoPhase) {
    super();
    this.acoPhase = acoPhase;
  }

  public DaemonActionType getAcoPhase() {
    return acoPhase;
  }

  public Environment getEnvironment() {
    return environment;
  }

  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  public AntColony<E> getAntColony() {
    return antColony;
  }

  public void setAntColony(AntColony<E> antColony) {
    this.antColony = antColony;
  }

  public abstract void applyDaemonAction(
      ConfigurationProvider configurationProvider);
}
