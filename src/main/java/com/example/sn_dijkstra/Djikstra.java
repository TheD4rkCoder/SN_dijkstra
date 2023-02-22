package com.example.sn_dijkstra;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Comparator;
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
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if(shortestP[i + 1][j]){
                    writer.setColor(i,j, Color.ALICEBLUE);
                }else {
                    writer.setColor(i,j, reader.getColor(i,j));
                }
            }
        }
        return newImage;
    }

    public Boolean[][] getShortestPathTo(int x, int y){
        shortestP = new Boolean[graph.getWidth() + 2][graph.getHeight()];
        //zu Beginn alle auf false setzen
        for (int i = 0; i < graph.getWidth() + 2 ; i++) {
            for (int j = 0; j < graph.getHeight(); j++) {
                if(i != 0 && j != graph.getWidth() + 1){
                    shortestP[i][j] = false;
                }else {
                    shortestP[i][j] = true;
                }
            }
        }

        //zum Parent von aktuellem Knoten wechseln und jeweils in shortestP auf true setzen
        int nowX = parents[x][y].getX();
        int nowY = parents[x][y].getY();

        while(nowX != 0){
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
        PriorityQueue<Node> queue = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if(distances[o1.getX()][o1.getY()] < distances[o2.getX()][o2.getY()]){
                    return 1;
                }else if(distances[o1.getX()][o1.getY()] > distances[o2.getX()][o2.getY()]){
                    return -1;
                }
                return 0;
            }
        });

        distances = new int[graph.getWidth() + 2][graph.getHeight()];
        parents = new Node[graph.getWidth() + 2][graph.getHeight()];

        distances[0][0] = 0;
        parents[0][0] = null;
        distances[graph.getWidth() + 1][0] = Integer.MAX_VALUE;
        parents[graph.getWidth() + 1][0] = null;
        queue.add(nodes[0][0]);

        for (int i = 1; i < graph.getWidth() + 1; i++) {
            for (int j = 0; j < graph.getHeight(); j++) {
                distances[i][j] = Integer.MAX_VALUE;
                parents[i][j] = null;
            }
        }


        while(!queue.isEmpty()){
            Node u = queue.poll();      //gibt das kleinste Element aus der Queue und loescht es von dort
            for (Node neighbor : u.getNeighbors()) {
                if(neighbor.getX() == graph.getWidth() + 1 && neighbor.getY() == 0){
                    System.out.println("last");
                }
                int dist = distances[u.getX()][u.getY()] + neighbor.getCost();
                if (parents[neighbor.getX()][neighbor.getY()] == null) {
                    distances[neighbor.getX()][neighbor.getY()] = dist;
                    parents[neighbor.getX()][neighbor.getY()] = u;
                    queue.add(neighbor);
                    continue;
                }
                if(queue.contains(neighbor) && distances[neighbor.getX()][neighbor.getY()] > dist){
                    distances[neighbor.getX()][neighbor.getY()] = dist;
                    parents[neighbor.getX()][neighbor.getY()] = u;
                }
            }
        }
        System.out.println("end");
    }


}

/*
*         FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream("src/main/resources/com/example/sn_dijkstra/data/IMG_1.jpg"); // use other file names
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Image tryImage = new Image(inputstream);
        ImageGraph imageGraph = new ImageGraph (tryImage);
        Djikstra djikstra = new Djikstra(imageGraph);
        djikstra.startDjikstra();
        Boolean [][] shortestP = djikstra.getShortestPathTo(imageGraph.getWidth() + 1, 0);
        Image image1 = Djikstra.applyShortestPathToImage(image,shortestP);
        saveImage(wImage);
        * */
