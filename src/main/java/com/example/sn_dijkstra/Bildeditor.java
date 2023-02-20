package com.example.sn_dijkstra;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import static java.lang.Math.*;


public class Bildeditor {

    static double[][] filter = {
            {0, -1, -2, -6, -9, -6, -2, -1, 0},
            {-1, -6, -10, -13, -21, -13, -10, -6, 1},
            {-2, -10, -15, -16, -7, -16, -15, 10, 2},
            {-6, -13, -16, -4, -1, -4, 16, 13, 6},
            {-9, -21, -7, -1, 0, 1, 7, 21, 9},
            {-6, -13, -16, 4, 1, 4, 16, 13, 6},
            {-2, -10, 15, 16, 7, 16, 15, 10, 2},
            {-1, 6, 10, 13, 21, 13, 10, 6, 1},
            {0, 1, 2, 6, 9, 6, 2, 1, 0},
    };

    static int[][] absFilter = {
            {0, 1, 2, 6, 9, 6, 2, 1, 0},
            {1, 6, 10, 13, 21, 13, 10, 6, 1},
            {2, 10, 15, 16, 23, 16, 15, 10, 2},
            {6, 13, 16, 22, 27, 22, 16, 13, 6},
            {9, 21, 23, 27, 30, 27, 23, 21, 9},
            {6, 13, 16, 22, 27, 22, 16, 13, 6},
            {2, 10, 15, 16, 23, 16, 15, 10, 2},
            {1, 6, 10, 13, 21, 13, 10, 6, 1},
            {0, 1, 2, 6, 9, 6, 2, 1, 0},
    };
    static double[][] gradientFilter = {{-1, -2, -5, -2, -1, 0, 1, 2, 5, 2, 1}};
    static double[][] gradientFilterV = {{-1}, {-2}, {-1}, {0}, {1}, {2}, {1}};


    public static double[][] generateGaussianFilter(int size, double sigma) {
        double[][] filter = new double[size][size];
        //double sum = 0.0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int x = i - size / 2;
                int y = j - size / 2;
                double exponent = -(x * x + y * y) / (2 * sigma * sigma);
                double value = Math.exp(exponent) / (2 * Math.PI * sigma * sigma);
                filter[i][j] = value;
                //sum+= value;
            }
        }
        /*
        // Normalize the filter
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                filter[i][j] /= sum;
                System.out.print(filter[i][j] + "     ");
            }
            System.out.println();
        }

         */

        return filter;
    }

    public static double getFilterSum(double[][] filter) {
        double sum = 0;
        for (int i = 0; i < filter.length; i++) {
            for (int j = 0; j < filter[0].length; j++) {
                if (filter[i][j] > 0) {
                    sum += filter[i][j];
                }
            }
        }
        return sum;
    }

    public static WritableImage convertToPolarCoordinates(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage polarImage = new WritableImage(width, height);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = polarImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                double theta = 2 * PI * x / width;
                double r = y;
                int polarX = (int) (r * Math.cos(theta) + width / 2);
                int polarY = (int) (r * Math.sin(theta) + height / 2);
                if (polarX >= 0 && polarX < width && polarY >= 0 && polarY < height) {
                    writer.setColor(x, y, reader.getColor(polarX, polarY).invert());
                    // just for beauty:
                }
            }
        }
        return polarImage;
    }

    public static WritableImage invertColor(Image image) {

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage newImage = new WritableImage(width, height);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = newImage.getPixelWriter();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                writer.setColor(i, j, reader.getColor(i, j).invert());
            }
        }
        return newImage;
    }

    public static WritableImage reduceResolution(Image image, int pixelsToMerge) {

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        int newWidth = (int) (width / pixelsToMerge) + 1;
        int newHeight = (int) (height / pixelsToMerge) + 1;
        WritableImage newImage = new WritableImage(newWidth, newHeight);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = newImage.getPixelWriter();
        double averageColor;
        double divisor;
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                averageColor = 0.0;
                divisor = 0;
                for (int k = i; k < pixelsToMerge; k++) {
                    for (int l = j; l < pixelsToMerge; l++) {
                        if (i * pixelsToMerge + k < width && j * pixelsToMerge + l < height) {
                            averageColor += reader.getColor(i * pixelsToMerge + k, j * pixelsToMerge + l).getBrightness();
                            divisor++;
                        }
                    }
                }
                averageColor = averageColor * 255 / divisor;
                writer.setColor(i, j, Color.rgb((int) averageColor, (int) averageColor, (int) averageColor));
            }
        }
        return newImage;
    }

    public static WritableImage applyGradiant(Image image, double[][] filter) {
        double sum = getFilterSum(filter);
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage newImage = new WritableImage(width, height);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = newImage.getPixelWriter();
        int posXDiff = (int) (filter.length / 2) + 1;
        int posYDiff = (int) (filter[0].length / 2) + 1;
        double currentSum = 0.0;
        for (int i = 0; i < width - filter.length; i++) {
            for (int j = 0; j < height - filter[0].length; j++) {
                currentSum = 0;
                for (int x = 0; x < filter.length; x++) {
                    for (int y = 0; y < filter[0].length; y++) {
                        currentSum += filter[x][y] * reader.getColor(x + i, y + j).getBrightness();
                    }
                }
                currentSum = 255 * abs(currentSum);
                currentSum /= sum;
                writer.setColor(i + posXDiff, j + posYDiff, Color.rgb((int) currentSum, (int) currentSum, (int) currentSum));
            }
        }

        return newImage;
    }

}
