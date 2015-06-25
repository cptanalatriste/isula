package isula.aco;

public abstract class AntPolicy<E> {

  // TODO(cgavidia): This should be used to program the execution of activities.
  private AntPolicyType policyType;
  private Ant<E> ant;

  public AntPolicy(AntPolicyType antPhase) {
    this.policyType = antPhase;
  }

  public AntPolicyType getPolicyType() {
    return policyType;
  }

  public void setAnt(Ant<E> ant) {
    this.ant = ant;
  }

  public Ant<E> getAnt() {
    return ant;
  }

  public abstract void applyPolicy(Environment environment,
      ConfigurationProvider configurationProvider);
}
