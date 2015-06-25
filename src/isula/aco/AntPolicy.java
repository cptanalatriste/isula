package isula.aco;

public abstract class AntPolicy {

  // TODO(cgavidia): This should be used to program the execution of activities.
  private AntPolicyType policyType;
  private Ant ant;

  public AntPolicy(AntPolicyType antPhase) {
    this.policyType = antPhase;
  }

  public AntPolicyType getPolicyType() {
    return policyType;
  }

  public void setAnt(Ant ant) {
    this.ant = ant;
  }

  public Ant getAnt() {
    return ant;
  }

  public abstract void applyPolicy(Environment environment,
      ConfigurationProvider configurationProvider);
}
