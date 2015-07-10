package isula.image.util;


public class ImageComparator {

  private String description;

  private double truePositives = 0;
  private double falsePositives = 0;
  private double falseNegatives = 0;
  private double trueNegatives = 0;
  private double intersectingPixels = 0;
  private double unionedPixels = 0;

  private String referenceImageFile;
  private String imageToValidateFile;
  private int greyScalePositiveThreshold;

  /**
   * Creates an instance for Compare images.
   * 
   * @param description Image descripcion.
   * @param referenceImageFile Image as a reference.
   * @param greyScalePositiveThreshold Threshold for positive values.
   */
  public ImageComparator(String description, String referenceImageFile,
      int greyScalePositiveThreshold) {
    this.description = description;
    this.referenceImageFile = referenceImageFile;
    this.greyScalePositiveThreshold = greyScalePositiveThreshold;
  }

  public String getReferenceImageFile() {
    return referenceImageFile;
  }

  /**
   * Extracts metrics about the similarities of two images.
   * 
   * @throws Exception
   *           In case file reading/writing fails.
   */
  public void executeComparison() throws Exception {
    truePositives = 0;
    falsePositives = 0;
    falsePositives = 0;
    intersectingPixels = 0;
    unionedPixels = 0;
    trueNegatives = 0;

    int[][] referenceImage = ImageFileHelper
        .getImageArrayFromFile(referenceImageFile);
    int[][] imageToValidate = ImageFileHelper
        .getImageArrayFromFile(imageToValidateFile);
    if (referenceImage.length != imageToValidate.length
        || referenceImage[0].length != imageToValidate[0].length) {
      throw new Exception("Images are not comparable");
    }

    for (int i = 0; i < referenceImage.length; i++) {
      for (int j = 0; j < referenceImage[0].length; j++) {

        if (referenceImage[i][j] >= greyScalePositiveThreshold
            && imageToValidate[i][j] >= greyScalePositiveThreshold) {
          truePositives++;
          intersectingPixels++;
          unionedPixels++;
        } else if (referenceImage[i][j] >= greyScalePositiveThreshold
            && imageToValidate[i][j] < greyScalePositiveThreshold) {
          falseNegatives++;
          unionedPixels++;
        } else if (referenceImage[i][j] < greyScalePositiveThreshold
            && imageToValidate[i][j] >= greyScalePositiveThreshold) {
          falsePositives++;
          unionedPixels++;
        } else if (referenceImage[i][j] < greyScalePositiveThreshold
            && imageToValidate[i][j] < greyScalePositiveThreshold) {
          trueNegatives++;
        }
      }
    }
  }

  public void setImageToValidateFile(String imageToValidateFile) {
    this.imageToValidateFile = imageToValidateFile;
  }

  public double getBuildingDetectionPercentage() {
    return truePositives / (truePositives + falseNegatives);
  }

  public double getBranchingFactor() {
    return falsePositives / truePositives;
  }

  public double getJaccardSimilarityIndex() {
    return intersectingPixels / unionedPixels;
  }

  public double getFalsePositiveRate() {
    return falsePositives / (falsePositives + trueNegatives);
  }

  public double getFalseNegativeRate() {
    return falseNegatives / (falseNegatives + truePositives);
  }

  /**
   * Returns the results as an String.
   * 
   * @return String representing the results.
   */
  public String resultAsString() {
    String result = description + ": BDP = " + getBuildingDetectionPercentage()
        + " BF = " + getBranchingFactor() + " JSI = "
        + getJaccardSimilarityIndex() + " FPR = " + getFalsePositiveRate()
        + " FNR = " + getFalseNegativeRate() + "\n";
    result += "Reference File ->" + referenceImageFile + "\n";
    result += "Generated File ->" + imageToValidateFile + "\n\n";

    return result;
  }

  public void setGreyScalePositiveThreshold(int greyScalePositiveThreshold) {
    this.greyScalePositiveThreshold = greyScalePositiveThreshold;
  }

}
