package isula.aco;

public abstract class AntPolicy {

  // TODO(cgavidia): This should be used to program the execution of activities.
  private AntPhase antPhase;

  public AntPolicy(AntPhase antPhase) {
    this.antPhase = antPhase;
  }

  public AntPhase getAntPhase() {
    return antPhase;
  }

  public abstract void applyPolicy(Environment environment, Ant ant);

}
