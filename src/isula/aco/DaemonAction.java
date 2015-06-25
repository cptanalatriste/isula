package isula.aco;

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
