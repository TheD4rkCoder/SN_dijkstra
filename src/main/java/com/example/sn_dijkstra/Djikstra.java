package com.example.sn_dijkstra;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.PriorityQueue;

public class Djikstra {
    private Boolean [][] shortestP;
    private int [][]distances;
    private Node [][]parents;

    private ImageGraph graph;

    public Djikstra(ImageGraph imageGraph) {
        this.graph = imageGraph;
    }

    public Boolean[][] getShortestP() {
        return shortestP;
    }

    public int[][] getDistances() {
        return distances;
    }

    public Node[][] getParents() {
        return parents;
    }

    public static WritableImage applyShortestPathToImage(Image image, Boolean[][] shortestP){
        WritableImage newImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = newImage.getPixelWriter();
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                if(shortestP[i][j]){
                    writer.setColor(i,j, Color.ALICEBLUE);
                }else {
                    writer.setColor(i,j, reader.getColor(i,j));
                }
            }
        }
        return newImage;
    }

    public Boolean[][] getShortestPathTo(int x, int y){
        shortestP = new Boolean[graph.getWidth()][graph.getHeight()];
        for (int i = 0; i < shortestP.length; i++) {
            for (int j = 0; j < graph.getWidth() + 2; j++) {
                if(i != 0 && j != 0 && j != graph.getWidth() + 1){
                    shortestP[i][j] = false;
                }else {
                    shortestP[i][j] = true;
                }
            }
        }

        int nowX = parents[x][y].getX();
        int nowY = parents[x][y].getY();
        while(nowX != 0 && nowY != 0){
            shortestP[nowX][nowY] = true;
            //Parent holen
            nowX = parents[nowX][nowY].getX();
            nowY = parents[nowX][nowY].getY();
        }
        return shortestP;
    }

    public void startDjikstra(){
        Node[][] nodes = graph.nodes;

        int nodeAmount = graph.getWidth() * graph.getHeight() + 2;  // +2 wegen ein Knoten zu Beginn und einen am Ende
        PriorityQueue<Node> queue = new PriorityQueue<>();

        distances = new int[graph.getHeight()][graph.getWidth()];
        parents = new Node[graph.getHeight()][graph.getWidth()];

        distances[0][0] = 0;
        parents[0][0] = null;
        queue.add(nodes[0][0]);

        for (int i = 1; i < graph.getWidth(); i++) {
            for (int j = 0; j < graph.getHeight(); j++) {
                distances[i][j] = Integer.MAX_VALUE;
                parents[i][j] = null;
            }
        }


        while(!queue.isEmpty()){
            Node u = queue.poll();      //gibt das kleinste Element aus der Queue und loescht es von dort
            for (Node neighbor : u.getNeighbors()) {
                int dist = distances[u.getX()][u.getY()] + neighbor.getCost();
                if(queue.contains(neighbor) && distances[neighbor.getX()][neighbor.getY()] > dist){
                    distances[neighbor.getX()][neighbor.getY()] = dist;
                    parents[neighbor.getX()][neighbor.getY()] = u;
                } else if (parents[neighbor.getX()][neighbor.getY()] == null) {
                    distances[neighbor.getX()][neighbor.getY()] = dist;
                    parents[neighbor.getX()][neighbor.getY()] = u;
                    queue.add(neighbor);
                }
            }
        }
    }


}
