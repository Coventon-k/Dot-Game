package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Player {
    private int number;
    private HashMap<Dot, ArrayList<Dot>> graph;  // Le Graphe est represente en forme de taleau
    private HashSet<Dot> pointsCapture; //Ensemble de points capturer
    private int nombreDotCapture;

    public Player(int number) {
        this.number = number;
        this.graph = new HashMap<>();
        this.pointsCapture = new HashSet<>();
    }


    public HashMap<Dot, ArrayList<Dot>> getMap() {
        return graph;
    }

    public HashSet<Dot> getPointsCapture() {
        return pointsCapture;
    }


    public int getNombreDotCapture() {
        nombreDotCapture = getPointsCapture().size();
        return nombreDotCapture;
    }
}
