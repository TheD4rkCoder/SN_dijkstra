package com.example.sn_dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Node {
    private final int x;
    private final int y;
    private int costLabel;
    private HashMap<Integer, Node> neighbors;
    private HashMap<Integer, Integer> costToNeighbors;
    private int counter = 0;

    public Node(int x, int y, int costLabel) {
        this.x = x;
        this.y = y;
        this.costLabel = costLabel;
        this.neighbors = new HashMap<>();
        this.costToNeighbors = new HashMap<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCostLabel() {
        return costLabel;
    }

    public void setCostLabel(int costLabel) {
        this.costLabel = costLabel;
    }

    public Node getNeighbor(int x) {
        return neighbors.get(x);
    }
    public int getEdgeToNeighborCost(int x) {
        return costToNeighbors.get(x);
    }


    public void addNeighbor(Node neighbor, int edgeCost) {
        neighbors.put(counter, neighbor);
        costToNeighbors.put(counter, edgeCost);
        counter++;
    }
    public int getAmountOfNeighbors() {
        return counter;
    }
}