package isula.image.util;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class ImageFileHelper {

    private ImageFileHelper() {

    }

    private static Logger logger = Logger.getLogger(ImageFileHelper.class
            .getName());

    public static final int ABSENT_PIXEL_FLAG = -1;
    public static final int GRAYSCALE_MIN_RANGE = 0;
    public static final int GRAYSCALE_MAX_RANGE = 255;

    private static final int ABSENT_PIXEL_CLUSTER = -1;
    private static final int[][] DEFAULT_STRUCTURING_ELEMENT = {
            {255, 255, 255}, {255, 255, 255}, {255, 255, 255}};
    private static final int GRAYSCALE_DELTA = 10;

    public static int[][] getImageArrayFromFile(String fileName)
            throws IOException {

        return getImageArrayFromFile(new File(fileName));
    }

    /**
     * Transforms an image into an array of ints.
     *
     * @param imageFile Image file.
     * @return An array of ints.
     * @throws IOException In case file reading fails.
     */
    public static int[][] getImageArrayFromFile(File imageFile)
            throws IOException {

        BufferedImage image = ImageIO.read(imageFile);
        Raster imageRaster = image.getData();

        int[][] imageAsArray;
        int[] pixel;
        int[] buffer = new int[1];

        imageAsArray = new int[imageRaster.getWidth()][imageRaster.getHeight()];

        for (int i = 0; i < imageRaster.getWidth(); i++) {
            for (int j = 0; j < imageRaster.getHeight(); j++) {
                pixel = imageRaster.getPixel(i, j, buffer);
                imageAsArray[i][j] = pixel[0];
            }
        }
        return imageAsArray;
    }

    /**
     * Generates an image file from an integer array.
     *
     * @param imageGraph      Array of ints representing the image.
     * @param outputImageFile Location of the generated file.
     * @throws IOException If file writing/reading fails.
     */
    public static void generateImageFromArray(int[][] imageGraph,
                                              String outputImageFile) throws IOException {
        logger.info("Generating output image");
        BufferedImage outputImage = new BufferedImage(imageGraph.length,
                imageGraph[0].length, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = outputImage.getRaster();
        for (int x = 0; x < imageGraph.length; x++) {
            for (int y = 0; y < imageGraph[x].length; y++) {
                if (imageGraph[x][y] != ABSENT_PIXEL_CLUSTER) {
                    raster.setSample(x, y, 0, imageGraph[x][y]);
                } else {
                    raster.setSample(x, y, 0, GRAYSCALE_MIN_RANGE);
                }
            }
        }

        File imageFile = new File(outputImageFile);
        ImageIO.write(outputImage, "bmp", imageFile);
        logger.log(Level.INFO, "Resulting image stored in: {0}", outputImageFile);
    }

    /**
     * Applies the open operator of mathematical morphology to an image.
     *
     * @param imageGraph          Image as an array on ints.
     * @param repetitionParameter Number of repetitions.
     * @return A new image after the application of the operator.
     */
    public static int[][] openImage(int[][] imageGraph, int repetitionParameter) {
        int[][] resultImage = imageGraph;

        for (int i = 0; i < repetitionParameter; i++) {
            resultImage = erodeImage(resultImage, DEFAULT_STRUCTURING_ELEMENT,
                    GRAYSCALE_MAX_RANGE, GRAYSCALE_MAX_RANGE / 2);
        }

        for (int i = 0; i < repetitionParameter; i++) {
            resultImage = dilateImage(resultImage, DEFAULT_STRUCTURING_ELEMENT,
                    GRAYSCALE_MAX_RANGE, GRAYSCALE_MAX_RANGE / 2);
        }

        return resultImage;
    }

    /**
     * Marks black pixels as background ones.
     *
     * @param imageGraph Image as an array of ints.
     * @return A new image, with black pixels marked as missing.
     */
    public static int[][] removeBackgroundPixels(int[][] imageGraph) {
        int[][] result = new int[imageGraph.length][imageGraph[0].length];
        for (int i = 0; i < imageGraph.length; i++) {
            for (int j = 0; j < imageGraph[0].length; j++) {
                if (Math.abs(imageGraph[i][j] - GRAYSCALE_MIN_RANGE) < GRAYSCALE_DELTA) {
                    result[i][j] = ABSENT_PIXEL_FLAG;
                } else {
                    result[i][j] = imageGraph[i][j];
                }
            }
        }
        return result;
    }

    /**
     * Applies the erosion operator of mathematical morphology.
     *
     * @param imageGraph         Image represented as an array of integers.
     * @param structuringElement Structuring element.
     * @param foregroundClass    Class for the foreground.
     * @param backgroundClass    Class for the background.
     * @return A new image, after the application of the operator.
     */
    public static int[][] erodeImage(int[][] imageGraph,
                                     int[][] structuringElement, int foregroundClass, int backgroundClass) {
        int[][] resultImage = new int[imageGraph.length][imageGraph[0].length];
        for (int i = 0; i < imageGraph.length; i++) {
            for (int j = 0; j < imageGraph[0].length; j++) {
                if (imageGraph[i][j] != ABSENT_PIXEL_CLUSTER) {
                    if (isFit(i, j, imageGraph, structuringElement, foregroundClass)) {
                        resultImage[i][j] = foregroundClass;
                    } else {
                        resultImage[i][j] = backgroundClass;
                    }
                } else {
                    resultImage[i][j] = ABSENT_PIXEL_CLUSTER;
                }

            }
        }

        return resultImage;
    }

    /**
     * Applies the dilation operator of mathematical morphology.
     *
     * @param imageGraph         Image represented as an array of integers.
     * @param structuringElement Structuring element.
     * @param foregroundClass    Class for the foreground.
     * @param backgroundClass    Class for the background.
     * @return A new image, after the application of the operator.
     */
    public static int[][] dilateImage(int[][] imageGraph,
                                      int[][] structuringElement, int foregroundClass, int backgroundClass) {
        int[][] resultImage = new int[imageGraph.length][imageGraph[0].length];
        for (int i = 0; i < imageGraph.length; i++) {
            for (int j = 0; j < imageGraph[0].length; j++) {
                if (imageGraph[i][j] != ABSENT_PIXEL_CLUSTER) {
                    if (isHit(i, j, imageGraph, structuringElement, foregroundClass)) {
                        resultImage[i][j] = foregroundClass;
                    } else {
                        resultImage[i][j] = backgroundClass;
                    }
                } else {
                    resultImage[i][j] = ABSENT_PIXEL_CLUSTER;
                }
            }

        }
        return resultImage;
    }

    /**
     * Identifies a pixel as a hit.
     *
     * @param row                Row position
     * @param column             Column position.
     * @param imageGraph         Image as an array of integers.
     * @param structuringElement Structuring element.
     * @param foregroundClass    Class of the foreground.
     * @return True if is a hit, false otherwise.
     */
    public static boolean isHit(int row, int column, int[][] imageGraph,
                                int[][] structuringElement, int foregroundClass) {
        int rowCounter = row - structuringElement.length / 2;
        int columnCounter = column - structuringElement[0].length / 2;
        int initialColumnValue = columnCounter;
        for (int k = 0; k < structuringElement.length; k++) {
            columnCounter = initialColumnValue;
            for (int l = 0; l < structuringElement[0].length; l++) {

                if (rowCounter >= 0 && rowCounter < imageGraph.length
                        && columnCounter >= 0 && columnCounter < imageGraph[0].length) {
                    if (structuringElement[k][l] == foregroundClass
                            && imageGraph[rowCounter][columnCounter] == foregroundClass) {
                        return true;
                    }
                }
                columnCounter++;
            }
            rowCounter++;
        }
        return false;

    }

    /**
     * Identifies a pixel as a fit.
     *
     * @param row                Row position
     * @param column             Column position.
     * @param imageGraph         Image as an array of integers.
     * @param structuringElement Structuring element.
     * @param foregroundClass    Class of the foreground.
     * @return True if is a hit, false otherwise.
     */
    public static boolean isFit(int row, int column, int[][] imageGraph,
                                int[][] structuringElement, int foregroundClass) {
        int rowCounter = row - structuringElement.length / 2;
        int columnCounter = column - structuringElement[0].length / 2;
        int initialColumnValue = columnCounter;
        for (int k = 0; k < structuringElement.length; k++) {
            columnCounter = initialColumnValue;
            for (int l = 0; l < structuringElement[0].length; l++) {
                if (rowCounter >= 0 && rowCounter < imageGraph.length
                        && columnCounter >= 0 && columnCounter < imageGraph[0].length) {
                    if (structuringElement[k][l] == foregroundClass
                            && imageGraph[rowCounter][columnCounter] != foregroundClass) {
                        return false;
                    }
                }
                columnCounter++;
            }
            rowCounter++;
        }
        return true;
    }

    /**
     * Returns a segment with the background removed.
     *
     * @param originalImage Original image.
     * @return An Image Segment.
     */
    public static ImageSegment getSegmentWithoutBackground(int[][] originalImage) {
        int grayScaleDelta = 10;
        int coordinateYLeftBoundary = -1;
        int coordinateYRightBoundary = -1;
        int coordinateXTopBoundary = -1;
        int coordinateXBottomBoundary = -1;

        int previousGreyScale = originalImage[originalImage.length / 2][0];
        for (int i = 1; i < originalImage[0].length; i++) {
            int currentGreyScale = originalImage[originalImage.length / 2][i];
            if (Math.abs(currentGreyScale - previousGreyScale) > grayScaleDelta
                    && coordinateYLeftBoundary < 0) {
                coordinateYLeftBoundary = i;
            } else if (Math.abs(currentGreyScale - previousGreyScale) > grayScaleDelta
                    && coordinateYLeftBoundary > 0) {
                coordinateYRightBoundary = i;
            }
        }

        previousGreyScale = originalImage[0][originalImage[0].length / 2];
        for (int i = 1; i < originalImage.length; i++) {
            int currentGreyScale = originalImage[i][originalImage[0].length / 2];
            if (Math.abs(currentGreyScale - previousGreyScale) > grayScaleDelta
                    && coordinateXTopBoundary < 0) {
                coordinateXTopBoundary = i;
            } else if (Math.abs(currentGreyScale - previousGreyScale) > grayScaleDelta
                    && coordinateXTopBoundary > 0) {
                coordinateXBottomBoundary = i;
            }
        }
        return new ImageSegment(coordinateYLeftBoundary, coordinateYRightBoundary,
                coordinateXTopBoundary, coordinateXBottomBoundary);
    }

    /**
     * Crops an image according to a segment.
     *
     * @param imageSegment  Segment to apply
     * @param originalImage Original image.
     * @return An image, cropped.
     */
    public static int[][] cropImage(ImageSegment imageSegment,
                                    int[][] originalImage) {
        int numberOfRows = imageSegment.getxBottomBoundary()
                - imageSegment.getxTopBoundary() + 1;
        int[][] croppedImage = new int[numberOfRows][];
        for (int i = 0, j = imageSegment.getxTopBoundary(); i < numberOfRows; i++, j++) {
            croppedImage[i] = Arrays.copyOfRange(originalImage[j],
                    imageSegment.getyLeftBoundary(), imageSegment.getyRightBoundary());
        }
        return croppedImage;
    }

    /**
     * Applies a filter to an image.
     *
     * @param imageGraph           Original image.
     * @param backgroundFilterMask Background filter.
     * @return A new image resulting of the application of the filter.
     * @throws IllegalArgumentException In case of an error.
     */
    public static int[][] applyFilter(int[][] imageGraph,
                                      int[][] backgroundFilterMask) throws IllegalArgumentException {

        if (imageGraph.length != backgroundFilterMask.length
                || imageGraph[0].length != backgroundFilterMask[0].length) {
            throw new IllegalArgumentException("Images are not comparable");
        }
        int[][] result = new int[imageGraph.length][imageGraph[0].length];
        for (int i = 0; i < imageGraph.length; i++) {
            for (int j = 0; j < imageGraph[0].length; j++) {
                if (backgroundFilterMask[i][j] == GRAYSCALE_MAX_RANGE / 2
                        || backgroundFilterMask[i][j] == ABSENT_PIXEL_CLUSTER) {
                    result[i][j] = ABSENT_PIXEL_FLAG;
                } else {
                    result[i][j] = imageGraph[i][j];
                }
            }
        }
        return result;
    }
}
