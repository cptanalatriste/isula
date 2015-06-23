package isula.aco;

public abstract class AntColonyPolicy {

  private AntColonyPhase acoPhase;

  public AntColonyPolicy(AntColonyPhase acoPhase) {
    super();
    this.acoPhase = acoPhase;
  }

  public AntColonyPhase getAcoPhase() {
    return acoPhase;
  }

  public abstract void applyPolicy(Environment environment, AntColony antColony);
}
