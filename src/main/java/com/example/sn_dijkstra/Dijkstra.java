package com.example.sn_dijkstra;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {
    private Boolean [][] shortestP;
    private Node [][]parents;
    private final ImageGraph graph;
    PriorityQueue<Node> queue = new PriorityQueue<Node>(new Comparator<Node>() {
        @Override
        public int compare(Node o1, Node o2) {  //aktuell den kleinsten Node am Anfang
            if(o1.getCostLabel() > o2.getCostLabel()){
                return 1;
            }else if(o1.getCostLabel() < o2.getCostLabel()){
                return -1;
            }
            return 0;
        }
    });

    public Dijkstra(ImageGraph imageGraph) {
        this.graph = imageGraph;
    }

    public Boolean[][] getShortestP() {
        return shortestP;
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
                    writer.setColor(i,j, Color.RED);
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
        for (int i = 1; i < graph.getWidth() + 1 ; i++) {
            for (int j = 0; j < graph.getHeight(); j++) {
                shortestP[i][j] = false;
            }
        }

        //zum Parent von aktuellem Knoten wechseln und jeweils in shortestP auf true setzen
        int nowX = parents[x][y].getX();
        int nowY = parents[x][y].getY();

        while(nowX != 0){
            shortestP[nowX][nowY] = true;
            //Parent holen
            int tempX = parents[nowX][nowY].getX();
            nowY = parents[nowX][nowY].getY();
            nowX = tempX;
        }
        return shortestP;
    }

    public void startDijkstra(){
        //parents-Arrays initialisieren
        parents = new Node[graph.getWidth() + 2][graph.getHeight()];
        parents[0][0] = null;
        parents[graph.getWidth() + 1][0] = null;
        for (int i = 1; i < graph.getWidth() + 1; i++) {
            for (int j = 0; j < graph.getHeight(); j++) {
                parents[i][j] = null;
            }
        }
        queue.add(graph.nodes[0][0]);

        //queue durchgehen, bis sie leer ist
        while(!queue.isEmpty()){
            Node u = queue.poll();   //gibt das kleinste Element aus der queue und loescht es von dort

            //alle Nachfolger des aktuellen Knotens u durchgehen
            for (int i = 0; i < u.getAmountOfNeighbors(); i++) {
                Node neighbor = u.getNeighbor(i);

                //dist = Kosten zu Nachfolger
                long dist = u.getCostLabel() + u.getEdgeToNeighborCost(i);

                //wenn Nachfolger keinen Parent hat, wird er in die queue gegeben
                if (parents[neighbor.getX()][neighbor.getY()] == null) {
                    //Aktualisieren der Kosten und Hinzufuegen von u als Parent
                    neighbor.setCostLabel(dist);
                    parents[neighbor.getX()][neighbor.getY()] = u;
                    queue.add(neighbor);

                    //wenn Nachfolger bereits in queue und dist kleiner als die aktuell eingetragenen Kosten
                    //werden die Kosten und das parent-Array aktualisiert
                } else if(queue.contains(neighbor) && neighbor.getCostLabel() > dist){
                    neighbor.setCostLabel(dist);
                    parents[neighbor.getX()][neighbor.getY()] = u;
                }
            }
        }
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
