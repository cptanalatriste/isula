package isula.image.util;

public class ImageSegment {
  int coordianteYLeftBoundary = -1;
  int coordianteYRightBoundary = -1;
  int coordinateXTopBoundary = -1;
  int coordinateXBottomBoundary = -1;

  /**
   * Generates a segment of an image.
   * 
   * @param coordianteYLeftBoundary
   *          Y coordinate for left boundary.
   * @param coordianteYRightBoundary
   *          Y coordinate for right boundary.
   * @param coordinateXTopBoundary
   *          X coordiante for top boundary.
   * @param coordinateXBottomBoundary
   *          X coordinate of bottom boundary.
   */
  public ImageSegment(int coordianteYLeftBoundary,
      int coordianteYRightBoundary, int coordinateXTopBoundary,
      int coordinateXBottomBoundary) {
    super();
    this.coordianteYLeftBoundary = coordianteYLeftBoundary;
    this.coordianteYRightBoundary = coordianteYRightBoundary;
    this.coordinateXTopBoundary = coordinateXTopBoundary;
    this.coordinateXBottomBoundary = coordinateXBottomBoundary;
  }

  public int getyLeftBoundary() {
    return coordianteYLeftBoundary;
  }

  public int getyRightBoundary() {
    return coordianteYRightBoundary;
  }

  public int getxTopBoundary() {
    return coordinateXTopBoundary;
  }

  public int getxBottomBoundary() {
    return coordinateXBottomBoundary;
  }

}
