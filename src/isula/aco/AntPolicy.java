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
 * @param <C>
 *          Class for components of a solution.
 * @param <E>
 *          Class representing the Environment.
 */
public abstract class AntPolicy<C, E extends Environment> {

  // TODO(cgavidia): This should be used to program the execution of activities.
  private AntPolicyType policyType;
  private Ant<C, E> ant;

  public AntPolicy(AntPolicyType antPhase) {
    this.policyType = antPhase;
  }

  public AntPolicyType getPolicyType() {
    return policyType;
  }

  public void setAnt(Ant<C, E> ant) {
    this.ant = ant;
  }

  public Ant<C, E> getAnt() {
    return ant;
  }

  public abstract void applyPolicy(E environment,
      ConfigurationProvider configurationProvider);
}
