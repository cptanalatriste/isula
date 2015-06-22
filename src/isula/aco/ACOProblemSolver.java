package isula.aco;

public abstract class ACOProblemSolver {

  private Environment environment;
  private AntColony antColony;
  private ConfigurationProvider configurationProvider;

  public Environment getEnvironment() {
    return environment;
  }

  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  public AntColony getAntColony() {
    return antColony;
  }

  public void setAntColony(AntColony antColony) {
    this.antColony = antColony;
  }

  public ConfigurationProvider getConfigurationProvider() {
    return configurationProvider;
  }

  public void setConfigurationProvider(
      ConfigurationProvider configurationProvider) {
    this.configurationProvider = configurationProvider;
  }

  public abstract void applyInitialConfigurationPolicies();

  public abstract void applyAfterSolutionConstructionPolicies();

}
