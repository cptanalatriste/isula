package isula.aco;

public abstract class ACOPolicy {

  private ACOPhase acoPhase;

  public ACOPolicy(ACOPhase acoPhase) {
    super();
    this.acoPhase = acoPhase;
  }

  public ACOPhase getAcoPhase() {
    return acoPhase;
  }

  public abstract void applyPolicy(Environment environment, AntColony antColony);
}
