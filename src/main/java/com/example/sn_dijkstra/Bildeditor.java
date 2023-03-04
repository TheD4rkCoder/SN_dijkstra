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

    static double[][] gradientFilterV = {{-1, -2, -4, -6, -4, -2, -1, 0, 1, 2, 4, 5, 4, 2, 1}};
    static double[][] gradientFilterH = {{-1}, {-2}, {-1}, {0}, {1}, {2}, {1}};
    static double[][] testFilter = {{-1, 0, 1}};


    public static double[][] generateGaussianFilter(int size, double sigma) {
        double[][] filter = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int x = i - size / 2;
                int y = j - size / 2;
                double exponent = -(x * x + y * y) / (2 * sigma * sigma);

                double value = Math.exp(exponent) / (Math.sqrt(2 * Math.PI) * sigma);

                filter[i][j] = value;
            }
        }
        return filter;
    }

//  double value = Math.exp(exponent) / (2 * Math.PI * sigma * sigma);
            /* gehört noch in generate gauss braucht es anscheinend nicht
        // Normalize the filter
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                filter[i][j] /= sum;
                System.out.print(filter[i][j] + "     ");
            }
            System.out.println();
        }

         */


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
        return convertToPolarCoordinates(image, (height > width) ? width / 2 : height / 2);
    }

    public static WritableImage convertToPolarCoordinates(Image image, int radius) {
        //Deklaration gebrauchter Konstanten
        final int width = (int) image.getWidth();
        final int height = (int) image.getHeight();

        //Resultat-Bild
        WritableImage polarImage = new WritableImage(width, radius);

        PixelReader reader = image.getPixelReader();
        PixelWriter writer = polarImage.getPixelWriter();

        //Beschreiben des polarImage
        for (int y = 0; y < radius; y++) {
            for (int x = 0; x < width; x++) {
                double theta = 2 * PI * x / width;
                double r = y;
                int polarX = (int) (r * Math.cos(theta) + width / 2);
                int polarY = (int) (r * Math.sin(theta) + height / 2);

                //beschreiben, wenn die Koordinaten im ursprünglichen Bild sind
                if (polarX >= 0 && polarX < width && polarY >= 0 && polarY < height) {
                    writer.setColor(x, y, reader.getColor(polarX, polarY));
                }
            }
        }
        return polarImage;
    }

    public static WritableImage convertToKartesianImage(Image image) {
        int width = (int) image.getWidth();
        int height = (int) (image.getHeight() * 2);

        WritableImage kartesianImage = new WritableImage(width, height);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = kartesianImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double angle = Math.atan((double) ((height >> 1) - y) / (double) (x - (width >> 1)));
                double r = Math.sqrt(Math.pow(x - (width >> 1), 2) + Math.pow(y - (width >> 1), 2));
                if (x < width >> 1) {
                    angle += PI;
                } else if (angle < 0) {
                    angle += 2 * PI;
                }
                angle = angle * width/(2 * PI);

                if (angle >= 0 && angle < width &&  r >= 0 &&  r < image.getHeight()) {
                    writer.setColor(x, height-y, reader.getColor((int)angle,(int) (r)));
                }
            }
        }
        return kartesianImage;

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
        int divisor;

        // jeden Pixel des neuen Bildes durchgehen
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                averageColor = 0.0;
                divisor = 0;

                // den Mittelwert der Farbwerte fuer den Pixel berechnen
                for (int k = 0; k < pixelsToMerge; k++) {
                    for (int l = 0; l < pixelsToMerge; l++) {
                        if (i * pixelsToMerge + k < width && j * pixelsToMerge + l < height) {
                            averageColor += reader.getColor(i * pixelsToMerge + k, j * pixelsToMerge + l).getBrightness();
                            divisor++;
                        }
                    }
                }
                averageColor = averageColor * 255.0 / divisor;
                writer.setColor(i, j, Color.rgb((int) averageColor, (int) averageColor, (int) averageColor));
            }
        }
        return newImage;
    }

    public static WritableImage saturateGrayscaleImage(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage newImage = new WritableImage(width, height);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = newImage.getPixelWriter();
        double minCost = 1;
        double maxCost = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double temp = reader.getColor(i, j).getBrightness();
                maxCost = Math.max(maxCost, temp);
                minCost = Math.min(minCost, temp);
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int brightness = (int) (255 * (reader.getColor(i, j).getBrightness() - minCost) / (maxCost - minCost));
                writer.setColor(i, j, Color.rgb(brightness, brightness, brightness));
            }
        }
        return newImage;
    }

    public static WritableImage applyFilter(Image image, double[][] filter) {
        // Deklaration gebrauchter Konstanten
        final int width = (int) image.getWidth();
        final int height = (int) image.getHeight();
        final int filterHalfX = filter.length / 2;
        final int filterHalfY = filter[0].length / 2;
        final double sum = getFilterSum(filter);
        // Deklaration gebrauchter Variablen
        double currentSum;
        // Resultat-Bild
        WritableImage newImage = new WritableImage(width, height);
        // für das lesen der Pixelwerte aus dem Ausgangsbild und dem Beschreiben des Resultat-Bilds
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = newImage.getPixelWriter();

        // Iteration über alle Pixel des Ausgangsbildes:
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // rücksetzen der Summe der Produkte vom Filter und Pixelwert
                currentSum = 0;
                // Iteration über alle Pixel rundherum um den aktuellen Pixel [i][j] anhand der größe des Filters
                for (int x = -filterHalfX; x <= filterHalfX; x++) {
                    for (int y = -filterHalfY; y <= filterHalfY; y++) {
                        if (x + i >= 0 && x + i < width && y + j >= 0 && y + j < height) {
                            // falls sich das aktuell angesehene Pixel innerhalb des Bildes befindet,
                            // wird zur Farbsumme das Produkt aus dem Pixel-Helligkeitswert und dem Filter-Wert an dieser Stelle addiert
                            currentSum += filter[x + filterHalfX][y + filterHalfY] * reader.getColor(x + i, y + j).getBrightness();
                        } else {
                            // falls sich das aktuell angesehene Pixel nicht innerhalb des Bildes befindet,
                            // wird die Farbe des am naheliegendsten Pixels für das Produkt verwendet und zur Farbsumme addiert
                            currentSum += filter[x + filterHalfX][y + filterHalfY] * reader.getColor((x + i < 0) ? 0 : (x + i < width) ?
                                    x + i : width - 1, (y + j < 0) ? 0 : (y + j < height) ? y + j : height - 1).getBrightness();
                        }
                    }
                }
                // Die Farbsumme wird mit 255 / (Summe des Filters) multipliziert um einen Farbwert zwischen 0 und 255 zu bekommen
                currentSum = 255 * abs(currentSum);
                currentSum /= sum;
                // das Aktuell angesehene Pixel wird mit der Summe als Graustufenwert beschrieben
                writer.setColor(i, j, Color.rgb((int) currentSum, (int) currentSum, (int) currentSum));
            }
        }
        return newImage;
    }

}
