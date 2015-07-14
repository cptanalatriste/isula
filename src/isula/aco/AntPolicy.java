package isula.aco;

/**
 * A behavior to be performed by an Ant. This class is used to implement
 * specific behaviours required by a particular ACO algorithm.
 * 
 * <p>
 * Each ant policy class has a policyType, which will define in which stage of
 * the construction process is executed.
 * 
 * @author Carlos G. Gavidia
 * 
 * @param <E>
 *          Class for components of a solution.
 */
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
