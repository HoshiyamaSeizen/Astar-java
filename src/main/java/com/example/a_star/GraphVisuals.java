package com.example.a_star;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains information about algorithm on current step
 * This information is used to visualize algorithm on graph
 */
public class GraphVisuals{
    private final Collection<Pair<Integer, Integer>> ignore;
    private final Collection<Pair<Integer, Integer>> visited;
    private final Collection<Pair<Integer, Integer>> path;
    private final Map<Integer, Pair<Double, Double>> heuristics;
    private int currentStep;

    public GraphVisuals() {
        this.ignore = new ArrayList<>();
        this.visited = new ArrayList<>();
        this.path = new ArrayList<>();
        this.heuristics = new HashMap<>();
        this.currentStep = 0;
    }

    public void addIgnore(Integer end, Integer start) { ignore.add(new Pair<>(end, start)); }
    public boolean shouldIgnore(Integer start, Integer end) {
        return edgeInCollection(start, end, ignore);
    }
    public void clearIgnore() { ignore.clear(); }

    public void update(int step /*, AStar alg */){
        //TODO: update collection according to step (step forward of backward)
    }

    public boolean edgeVisited(Integer start, Integer end){
        return edgeInCollection(start, end, visited);
    }

    public boolean edgeInPath(Integer start, Integer end){
        return  edgeInCollection(start, end, path);
    }

    public Map<Integer, Pair<Double, Double>> getHeuristics(){
        return heuristics;
    }
    public Pair<Double, Double> getHeuristicsNode(int id){
        return heuristics.get(id);
    }

    private boolean edgeInCollection(Integer start, Integer end, Collection<Pair<Integer, Integer>> collection){
        for (Pair<Integer, Integer> pair : collection)
            if(pair.getKey().equals(start) && pair.getValue().equals(end))
                return true;
        return false;
    }

    public void clear(){
        currentStep = 0;
        visited.clear();
        path.clear();
        heuristics.clear();
    }
}
