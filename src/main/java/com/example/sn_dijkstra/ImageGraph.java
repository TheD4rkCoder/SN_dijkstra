package com.example.sn_dijkstra;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
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

    public static ImageGraph startConverting() {
        String filename = "img1.png";
        return new ImageGraph(filename);    //mocht a output.png, lei um zu schaugen obs geat

    }

    public ImageGraph(String filename) {
        try {
            BufferedImage image = ImageIO.read(new File(filename));
            width = image.getWidth();
            height = image.getHeight();
            nodes = new Node[width + 2][height];    //width + 2 wegen Anfangs und Endknoten

            nodes[0][0] = new Node(0,0,0);      //Startknoten einfuegen
            nodes[width + 1][0] = new Node(width + 1,0,Integer.MAX_VALUE);      //Endknoten einfuegen

            // create a Node for each pixel
            for (int i = 1; i < width - 1; i++) {
                for (int j = 0; j < height; j++) {
                    Color c = new Color(image.getRGB(i, j));
                    int brightness = (int) (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue());
                    nodes[i][j] = new Node(i, j, brightness);
                }
            }

            //Umbauen nou auf gerichtet
            // connect adjacent nodes with weighted edges
            for (int i = 1; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (i > 1) {
                        nodes[i][j].addNeighbor(nodes[i - 1][j]);
                    }
                    if (j > 0) {
                        nodes[i][j].addNeighbor(nodes[i][j - 1]);
                    }
                    if (i < width) {
                        nodes[i][j].addNeighbor(nodes[i + 1][j]);
                    }
                    if (j < height - 1) {
                        nodes[i][j].addNeighbor(nodes[i][j + 1]);
                    }
                }
            }
            //fuer ersten Knoten alle folgenden als Neighbors eintragen alle
            for (int i = 0; i < height; i++) {
                nodes[0][0].addNeighbor(nodes[1][i]);
            }
            //fuer alle vorletzten Knoten denletzten Knoten als Neighbor anhaengen
            for (int i = 0; i < height; i++) {
                nodes[width][i].addNeighbor(nodes[width + 1][0]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        drawGraphImage("outout.png");
    }

    public void drawGraphImage(String fileName) {
        BufferedImage graphImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Draw nodes as small squares
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Node node = nodes[i+1][j];
                int colorValue = node.getCost(); // Grayscale color
                Color nodeColor = new Color(colorValue, colorValue, colorValue);
                for (int x = i - 1; x <= i + 1; x++) {
                    for (int y = j - 1; y <= j + 1; y++) {
                        if (x >= 0 && x < width && y >= 0 && y < height) {
                            graphImage.setRGB(x, y, nodeColor.getRGB());
                        }
                    }
                }
            }
        }

        // Draw edges as lines
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Node node = nodes[i+1][j];
                for (Node neighbor : node.getNeighbors()) {
                    int x1 = node.getX();
                    int y1 = node.getY();
                    int x2 = neighbor.getX();
                    int y2 = neighbor.getY();
                    int brightness = (node.getCost() + neighbor.getCost()) / 2; // Average brightness
                    Color edgeColor = new Color(brightness, brightness, brightness);
                    drawLine(graphImage, x1, y1, x2, y2, edgeColor);
                }
            }
        }

        // Save the image to file
        try {
            ImageIO.write(graphImage, "png", new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawLine(BufferedImage image, int x1, int y1, int x2, int y2, Color color) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;
        int x = x1;
        int y = y1;
        while (x != x2 || y != y2) {
            image.setRGB(x, y, color.getRGB());
            int e2 = err * 2;
            if (e2 > -dy) {
                err -= dy;
                x += sx;
            }
            if (e2 < dx) {
                err += dx;
                y += sy;
            }
        }
    }
}

/*
package com.example.sn_dijkstra;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageGraph {
    private Node[][] nodes;
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

    public static ImageGraph startConverting() {
        String filename = "img1.png";
        return new ImageGraph(filename);    //mocht a output.png, lei um zu schaugen obs geat

    }

    public ImageGraph(String filename) {
        try {
            BufferedImage image = ImageIO.read(new File(filename));
            width = image.getWidth();
            height = image.getHeight();
            nodes = new Node[width][height];

            // create a Node for each pixel
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Color c = new Color(image.getRGB(i, j));
                    int brightness = (int) (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue());
                    nodes[i][j] = new Node(i, j, brightness);
                }
            }

            // connect adjacent nodes with weighted edges
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (i > 0) {
                        nodes[i][j].addNeighbor(nodes[i - 1][j]);
                    }
                    if (j > 0) {
                        nodes[i][j].addNeighbor(nodes[i][j - 1]);
                    }
                    if (i < width - 1) {
                        nodes[i][j].addNeighbor(nodes[i + 1][j]);
                    }
                    if (j < height - 1) {
                        nodes[i][j].addNeighbor(nodes[i][j + 1]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        drawGraphImage("outout.png");
    }

    public void drawGraphImage(String fileName) {
        BufferedImage graphImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Draw nodes as small squares
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Node node = nodes[i][j];
                int colorValue = node.getCost(); // Grayscale color
                Color nodeColor = new Color(colorValue, colorValue, colorValue);
                for (int x = i - 1; x <= i + 1; x++) {
                    for (int y = j - 1; y <= j + 1; y++) {
                        if (x >= 0 && x < width && y >= 0 && y < height) {
                            graphImage.setRGB(x, y, nodeColor.getRGB());
                        }
                    }
                }
            }
        }

        // Draw edges as lines
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Node node = nodes[i][j];
                for (Node neighbor : node.getNeighbors()) {
                    int x1 = node.getX();
                    int y1 = node.getY();
                    int x2 = neighbor.getX();
                    int y2 = neighbor.getY();
                    int brightness = (node.getCost() + neighbor.getCost()) / 2; // Average brightness
                    Color edgeColor = new Color(brightness, brightness, brightness);
                    drawLine(graphImage, x1, y1, x2, y2, edgeColor);
                }
            }
        }

        // Save the image to file
        try {
            ImageIO.write(graphImage, "png", new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawLine(BufferedImage image, int x1, int y1, int x2, int y2, Color color) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;
        int x = x1;
        int y = y1;
        while (x != x2 || y != y2) {
            image.setRGB(x, y, color.getRGB());
            int e2 = err * 2;
            if (e2 > -dy) {
                err -= dy;
                x += sx;
            }
            if (e2 < dx) {
                err += dx;
                y += sy;
            }
        }
    }
}
 */