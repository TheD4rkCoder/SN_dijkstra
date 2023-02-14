package com.example.sn_dijkstra;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import static java.lang.Math.PI;
import static java.lang.Math.sin;



public class Bildeditor {

    static int[][] filter = {
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

    public static int getFilterSum(int [][]filter){
        int sum = 0;
        for (int i = 0; i < filter.length; i++) {
            for (int j = 0; j < filter.length; j++) {
                sum += filter[i][j];
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

                double theta = 2*PI * x / width;
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
    public static WritableImage applyGradiant(Image image) {
        int[][] temp = new int[9][9];
        int sum = 0;
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++) {

            }
        }
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage newImage = new WritableImage(width, height);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = newImage.getPixelWriter();
        int posXDiff = (int) (temp.length/2) + 1;
        int posYDiff = (int) (temp[0].length/2) + 1;
        for (int i = 0; i < width-temp.length; i++) {
            for (int j = 0; j < width-temp[0].length; j++) {
            }
        }


        return newImage;
    }

}
