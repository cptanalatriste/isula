package isula.aco.exception;

/**
 * Used when the instances used to solve the problem are not configured
 * properly.
 * 
 * @author Carlos G. Gavidia
 * 
 */
public class ConfigurationException extends RuntimeException {

  private static final long serialVersionUID = 6030185194935219341L;

  public ConfigurationException(String string) {
    super(string);
  }

}
