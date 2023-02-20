package com.example.sn_dijkstra;


import java.util.PriorityQueue;

public class Djikstra {
    private Boolean [][] shortestP;
    private int [][]distances;
    private Node [][]parents;

    public Boolean[][] getShortestP() {
        return shortestP;
    }

    public int[][] getDistances() {
        return distances;
    }

    public Node[][] getParents() {
        return parents;
    }

    public void getShortestPathTo(int x, int y){
        int nowX = x;
        int nowY = y;
        while(nowX != 0 && nowY != 0){
            shortestP[nowX][nowY] = true;
            //Parent holen
            nowX = parents[nowX][nowY].getX();
            nowY = parents[nowX][nowY].getY();
        }
    }

    public void startDjikstra(ImageGraph graph){
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
