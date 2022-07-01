package com.example.a_star;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Graph {
    private final Map<Integer, Collection<Pair<Integer, Double>>> edgesInfo;
    private final Map<Integer, Pair<Double, Double>> verticesInfo;
    private final Collection<Pair<Integer, Integer>> ignore;

    public Graph(){
        edgesInfo = new HashMap<>();
        verticesInfo = new HashMap<>();
        ignore = new ArrayList<>();
    }

    public void addEdge(Integer start, Integer end, Double weight){
        edgesInfo.putIfAbsent(start, new ArrayList<>());
        edgesInfo.get(start).add(new Pair<>(end, weight));
    }

    public void setEdges(Integer start, Collection<Pair<Integer, Double>> collection){
        edgesInfo.put(start, collection);
    }

    public void addVertex(Integer id, Double x, Double y){
        verticesInfo.putIfAbsent(id, new Pair<>(x, y));
    }
    public void addVertex(Integer id, Pair<Double, Double> pair){ verticesInfo.putIfAbsent(id, pair); }

    public Pair<Double, Double> getVertex(Integer id){
        return verticesInfo.get(id);
    }

    public void removeEdge(Integer start, Integer end){
        Collection<Pair<Integer, Double>> collection;
        if((collection = edgesInfo.get(start)) != null){
            collection.removeIf(pair -> pair.getKey().equals(end));
        }
    }

    public void removeVertex(Integer id){
        verticesInfo.remove(id);
        edgesInfo.remove(id);
        for(Integer key: edgesInfo.keySet())
            removeEdge(key, id);
    }

    public boolean edgeExists(Integer start, Integer end){
        return findPair(start, end) != null;
    }

    public boolean vertexExists(Integer v){
        return verticesInfo.containsKey(v);
    }

    public Double getWeight(Integer start, Integer end){
        Pair<Integer, Double> pair;
        return (pair = findPair(start, end)) != null? pair.getValue() : 0;
    }

    public Integer[] getAdjVertices(Integer id){
        Collection<Pair<Integer, Double>> collection;
        if((collection = edgesInfo.get(id)) != null) {
            Integer[] vertices = new Integer[collection.size()];
            int i = 0;
            for (Pair<Integer, Double> pair : collection)
                vertices[i++] = pair.getKey();
            return  vertices;
        }
        return null;
    }

    public Map<Integer, Pair<Double, Double>> getVerticesInfo() { return verticesInfo; }
    public Map<Integer, Collection<Pair<Integer, Double>>> getEdgesInfo() { return edgesInfo; }
    public Collection<Pair<Integer, Double>> getEdges(Integer key){
        return edgesInfo.get(key);
    }

    private Pair<Integer, Double> findPair(Integer start, Integer end){
        Collection<Pair<Integer, Double>> collection;
        if((collection = edgesInfo.get(start)) != null){
            for (Pair<Integer, Double> pair : collection) {
                if (pair.getKey().equals(end)) return pair;
            }
        }
        return null;
    }

    public void addIgnore(Integer end, Integer start) { ignore.add(new Pair<>(end, start)); }
    public boolean shouldIgnore(Integer start, Integer end) {
        for (Pair<Integer, Integer> pair : ignore)
            if(pair.getKey().equals(start) && pair.getValue().equals(end))
                return true;
        return false;
    }
    public void clearIgnore() { ignore.clear(); }

    public void clear(){
        edgesInfo.clear();
        verticesInfo.clear();
        clearIgnore();
    }
}


