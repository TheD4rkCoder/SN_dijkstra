package com.example.sn_dijkstra;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageGraph {
    public Node[][] nodes;
    private int width;
    private int height;


    public Node[][] getNodes() {
        return nodes;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ImageGraph(Image image) {

        PixelReader reader = image.getPixelReader();
        width = (int) image.getWidth();
        height = (int) image.getHeight();
        nodes = new Node[width + 2][height];    //width + 2 wegen Anfangs- und Endknoten

        nodes[0][0] = new Node(0, 0, 0);                              //Startknoten einfuegen
        nodes[width + 1][0] = new Node(width + 1, 0, Integer.MAX_VALUE);      //Endknoten einfuegen

        // Erstellen der Nodes fuer jedes Pixel
        for (int i = 1; i < width + 1; i++) {
            for (int j = 0; j < height; j++) {
                nodes[i][j] = new Node(i, j, Integer.MAX_VALUE);
            }
        }
        // jedem Pixel die Nachfolger anhaengen
        for (int i = 1; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < height; k++) {
                    nodes[i][j].addNeighbor(nodes[i + 1][k], (int) (100.0 *(0.5 + reader.getColor(i, k).getBrightness()) * 100 - Math.pow(2, Math.abs(j - k))));
                }
            }
        }
        //ersten Knoten an erste Spalte anhaengen
        for (int i = 0; i < height; i++) {
            nodes[0][0].addNeighbor(nodes[1][i], (int) (100.0 * reader.getColor(0, i).getBrightness()));
            nodes[width][i].addNeighbor(nodes[width+1][0], 0);
        }
    }
}
