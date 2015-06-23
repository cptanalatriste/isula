package isula.aco;

public abstract class AcoPolicy {

  private AcoPhase acoPhase;

  public AcoPolicy(AcoPhase acoPhase) {
    super();
    this.acoPhase = acoPhase;
  }

  public AcoPhase getAcoPhase() {
    return acoPhase;
  }

  public abstract void applyPolicy(Environment environment, AntColony antColony);
}
