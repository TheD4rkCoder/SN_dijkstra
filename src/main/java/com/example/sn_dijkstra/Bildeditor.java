package com.example.sn_dijkstra;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

public class Bildeditor {
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
                    writer.setColor(x, y, reader.getColor(polarX, polarY));
                    writer.setColor(width-x-1, height-y-1, reader.getColor(polarX, polarY));
                }
            }
        }
        return polarImage;
    }
}
