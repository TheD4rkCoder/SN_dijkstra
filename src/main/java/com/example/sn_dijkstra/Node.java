package com.example.sn_dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Node {
    private final int x;
    private final int y;
    private int cost;
    private HashMap<Integer, Node> neighbors;
    private int counter = 0;

    public Node(int x, int y, int cost) {
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.neighbors = new HashMap<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public HashMap<Integer,Node> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(Node neighbor) {
        neighbors.put(counter, neighbor);
        counter++;
    }
    public int getAmountOfNeighbors() {
        return counter;
    }
}