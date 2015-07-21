package isula.image.util;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

public class ClusteredPixel {

  private ImagePixel imagePixel;
  private int cluster = ImagePixel.INITIAL_VALUE;

  /**
   * Creates a new pixel and associates it to a Cluster.
   * 
   * @param coordinateForX
   *          X coordinate.
   * @param coordinateForY
   *          Y coordinate.
   * @param imageGraph
   *          Graph representing the image.
   * @param cluster
   *          Assigned cluster.
   */
  public ClusteredPixel(int coordinateForX, int coordinateForY,
      double[][] imageGraph, int cluster) {
    super();
    this.imagePixel = new ImagePixel(coordinateForX, coordinateForY, imageGraph);
    this.cluster = cluster;
  }

  public int getxCoordinate() {
    return imagePixel.getxCoordinate();
  }

  public void setxCoordinate(int coordinateForX) {
    this.imagePixel.setxCoordinate(coordinateForX);
  }

  public int getyCoordinate() {
    return imagePixel.getyCoordinate();
  }

  public void setyCoordinate(int coordinateForY) {
    this.imagePixel.setyCoordinate(coordinateForY);
  }

  public int getGreyScaleValue() {
    return imagePixel.getGreyScaleValue();
  }

  public void setGreyScaleValue(int greyScaleValue) {
    this.imagePixel.setGreyScaleValue(greyScaleValue);
  }

  public int getCluster() {
    return cluster;
  }

  public void setCluster(int cluster) {
    this.cluster = cluster;
  }

  public String toString() {
    return "Pixel " + this.imagePixel.toString() + " to Cluster: " + cluster;
  }

  /**
   * Return a neighborhood in reference to a partition.
   * 
   * @param partition
   *          Partition.
   * @param imageGraph
   *          Graph of the image.
   * @return Neighbours of the current pixel.
   */
  public List<ClusteredPixel> getNeighbourhood(ClusteredPixel[] partition,
      double[][] imageGraph) {

    ArrayList<ClusteredPixel> neighbours = new ArrayList<ClusteredPixel>();
    List<ImagePixel> pixelNeighbours = this.imagePixel
        .getNeighbourhood(imageGraph);

    for (ImagePixel neighbour : pixelNeighbours) {
      ClusteredPixel posiblePixel = new ClusteredPixel(
          neighbour.getxCoordinate(), neighbour.getyCoordinate(), imageGraph,
          ImagePixel.INITIAL_VALUE);
      verifyPartitionAndAdd(partition, neighbours, posiblePixel, imageGraph);

    }

    return neighbours;
  }

  private void verifyPartitionAndAdd(ClusteredPixel[] partition,
      ArrayList<ClusteredPixel> neighbours, ClusteredPixel posiblePixel,
      double[][] imageGraph) {
    ClusteredPixel pixelInPartition = partition[posiblePixel.getxCoordinate()
        * imageGraph[0].length + posiblePixel.getyCoordinate()];
    if (pixelInPartition != null) {
      posiblePixel.setCluster(pixelInPartition.getCluster());
      neighbours.add(posiblePixel);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }

    ClusteredPixel visitedPixel = (ClusteredPixel) obj;

    return new EqualsBuilder()
        .append(this.getxCoordinate(), visitedPixel.getxCoordinate())
        .append(this.getyCoordinate(), visitedPixel.getyCoordinate())
        .append(this.getGreyScaleValue(), visitedPixel.getGreyScaleValue())
        .append(this.getCluster(), visitedPixel.getCluster()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this.getxCoordinate())
        .append(this.getyCoordinate()).append(this.getGreyScaleValue())
        .append(this.getCluster()).toHashCode();
  }

}
