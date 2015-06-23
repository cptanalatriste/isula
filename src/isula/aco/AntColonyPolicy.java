package isula.aco;

public abstract class AntColonyPolicy {

  private AcoPhase acoPhase;

  public AntColonyPolicy(AcoPhase acoPhase) {
    super();
    this.acoPhase = acoPhase;
  }

  public AcoPhase getAcoPhase() {
    return acoPhase;
  }

  public abstract void applyPolicy(Environment environment, AntColony antColony);
}
