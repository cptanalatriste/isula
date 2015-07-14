package isula.aco.exception;

/**
 * Produced when errors appear while ants are constructing a solution.
 * 
 * @author Carlos G. Gavidia
 * 
 */
public class SolutionConstructionException extends RuntimeException {

  private static final long serialVersionUID = 6984098059181599560L;

  public SolutionConstructionException(String string) {
    super(string);
  }

}
