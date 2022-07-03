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
    private AStar results;
    private final ArrayList<Pair<Integer, Integer>> ignore;
    private final ArrayList<Pair<Integer, Integer>> visited;
    private final Map<Integer, Pair<Double, Double>> heuristics;
    private int currentStep;

    public GraphVisuals() {
        this.ignore = new ArrayList<>();
        this.visited = new ArrayList<>();
        this.heuristics = new HashMap<>();
        this.currentStep = 0;
    }

    public void setResults(AStar results){
        this.results = results;
    }

    public void addIgnore(Integer end, Integer start) {
        ignore.add(new Pair<>(end, start));
    }
    public boolean shouldIgnore(Integer start, Integer end) {
        return edgeInCollection(start, end, ignore);
    }
    public void clearIgnore() { ignore.clear(); }

    public void update(){
        int step = Math.min(State.getStep(), results.getCountSteps());
        if(step > this.currentStep){
            visited.addAll(results.getEdgeSteps().subList(currentStep, step));
            for(int i = currentStep; i < step; i++)
                heuristics.putAll(results.getHeuristicSteps().get(i));
        }else if(step < this.currentStep){
            visited.subList(step, currentStep).clear();
            for(int i = step; i < currentStep; i++) {
                heuristics.keySet().removeAll(results.getHeuristicSteps().get(i).keySet());
            }
        }
        currentStep = step;
        if(step == results.getCountSteps()){
            State.setAlgFinished(true);
            State.setAlgPaused(true);
        }
        State.setAlgFinished(step == results.getCountSteps());
        State.setAlgPaused(State.isAlgPaused() || State.isAlgFinished());
    }

    private boolean edgeVisited(Edge edge){
        return edgeInCollection(edge.getStartID(), edge.getEndID(), visited)
                || edgeInCollection(edge.getEndID(), edge.getStartID(), visited);
    }

    private boolean edgeInPath(Edge edge){
        return (currentStep == results.getCountSteps())
                && (edgeInCollection(edge.getStartID(), edge.getEndID(), results.getFinalPath())
                || edgeInCollection(edge.getEndID(), edge.getStartID(), results.getFinalPath()));
    }

    private Pair<Double, Double> getHeuristicsNode(Node node){
        return heuristics.get(node.id());
    }

    private boolean edgeInCollection(Integer start, Integer end, Collection<Pair<Integer, Integer>> collection){
        for (Pair<Integer, Integer> pair : collection)
            if(pair.getKey().equals(start) && pair.getValue().equals(end))
                return true;
        return false;
    }

    public void check(Edge edge){
        if(edgeInPath(edge))
            edge.setEdgeStyle("-fx-stroke-width: 3; -fx-stroke: #FFFF00;");
        else if (edgeVisited(edge))
            edge.setEdgeStyle("-fx-stroke-width: 2; -fx-stroke: #00FF00;");
    }

    public void check(Node node){
        Pair<Double, Double> pair = getHeuristicsNode(node);
        if(pair != null)
            node.setLabel(pair.getKey(), pair.getValue());
    }

    public void clear(){
        results = null;
        currentStep = 0;
        visited.clear();
        heuristics.clear();
    }
}
