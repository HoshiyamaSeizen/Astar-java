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

        printInfo();

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

    private void printInfo() {
        String res;
        if(currentStep == 0){
            res = "Начало алгоритма. Находимся в вершине " + results.getPathEnds().getKey()
                    + ".\nНеобходимо дойти до вершины " + results.getPathEnds().getValue();
        }else if(currentStep == results.getCountSteps()){
            String path = "из вершины " + results.getPathEnds().getKey() + " в вершину " + results.getPathEnds().getValue();
            res = "Конец алгоритма. Алгоритм был пройден за " + results.getCountSteps() + " шагов.\n"
                    + (results.getPathLen() > 0 ? "Был найден путь " + path + " длиной " + results.getPathLen()
                    : "Путь " + path + " не был найден" );
        }else{
            res = "Рассматриваем ещё неучтённые соседние вершины: "
                    + results.getHeuristicSteps().get(currentStep-1).keySet()
                    + "\nПереходим в вершину с минимальной эврестической оценкой: "
                    + results.getEdgeSteps().get(currentStep-1).getValue();
        }
        Message.setMsg(res);
    }
}
