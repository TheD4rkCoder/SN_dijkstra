package com.example.sn_dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

class Node {
    private final int x;
    private final int y;
    private long costLabel;
    private final HashMap<Integer, Node> neighbors;
    private final HashMap<Integer, Long> costToNeighbors;
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
    public long getCostLabel() {
        return costLabel;
    }
    public void setCostLabel(long costLabel) {
        this.costLabel = costLabel;
    }
    public Node getNeighbor(int x) {
        return neighbors.get(x);
    }
    public long getEdgeToNeighborCost(int x) {
        return costToNeighbors.get(x);
    }
    public int getAmountOfNeighbors() {
        return counter;
    }
    public void addNeighbor(Node neighbor, long edgeCost) {
        neighbors.put(counter, neighbor);
        costToNeighbors.put(counter, edgeCost);
        counter++;
    }

}

