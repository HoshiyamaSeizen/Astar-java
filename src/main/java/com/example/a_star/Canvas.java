package com.example.a_star;

import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

import static com.example.a_star.Choice.*;

public class Canvas {
    private final Pane canvasPane;
    private final Graph graph;
    private int nodesCreated;
    private Integer selectedNode;
    public Canvas(Pane pane){
        this.canvasPane = pane;
        this.graph = new Graph();
        nodesCreated = 0;
    }

    public void addNode(double x, double y){
        if(x > Node.radius && x < canvasPane.getWidth()-Node.radius &&
                y > Node.radius && y < canvasPane.getHeight()-Node.radius){
            graph.addVertex(++nodesCreated, x, y);
            redraw();
        }
    }

    public void addEdge(Integer node1, Integer node2){
        if(!graph.edgeExists(node1, node2)) {
            double weight = requestWeight();
            if(weight > 0) {
                graph.addEdge(node1, node2, weight);
                redraw();
            }
        }
    }

    private void handleNodeClick(MouseEvent e, Node node){
        if (e.getButton() == MouseButton.PRIMARY && checkAction(ACTION.CONNECT)) {
            if(selectedNode != null && selectedNode != node.id()){
                addEdge(selectedNode, node.id());
                selectedNode = null;
            }else{
                selectedNode = node.id();
            }
        } else if(e.getButton() == MouseButton.PRIMARY && checkAction(ACTION.DELETE)) {
            graph.removeVertex(node.id());
            redraw();
        }
    }

    private void handleEdgeClick(MouseEvent e, Edge edge){
        if(e.getButton() == MouseButton.PRIMARY && checkAction(ACTION.DELETE)) {
            graph.removeEdge(edge.getStartID(), edge.getEndID());
            graph.removeEdge(edge.getEndID(), edge.getStartID());
            redraw();
        }
    }

    private double requestWeight(){
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle(null);
        dialog.setContentText(null);
        dialog.setHeaderText("Enter Weight of the Edge:");
        return dialog.showAndWait().map(Double::parseDouble).orElse(0.0);
    }

    private void redraw(){
        canvasPane.getChildren().clear();
        graph.clearIgnore();

        Map<Integer, Pair<Double, Double>> verticesInfo = graph.getVerticesInfo();
        Map<Integer, Collection<Pair<Integer, Double>>> edgesInfo = graph.getEdgesInfo();

        redrawEdges(verticesInfo, edgesInfo);
        redrawVertices(verticesInfo);
    }

    private void redrawVertices(Map<Integer, Pair<Double, Double>> verticesInfo) {
        for(Integer id: verticesInfo.keySet()){
            Pair<Double, Double> pair = verticesInfo.get(id);
            Node node = new Node(id, pair.getKey(), pair.getValue());
            canvasPane.getChildren().add(node);
            node.setOnMouseClicked(e -> handleNodeClick(e, node));
        }
    }

    private void redrawEdges(Map<Integer, Pair<Double, Double>> verticesInfo, Map<Integer, Collection<Pair<Integer, Double>>> edgesInfo) {
        for(Integer start: edgesInfo.keySet()){
            Pair<Double, Double> pairS = verticesInfo.get(start);
            Collection<Pair<Integer, Double>> collection;
            if((collection = edgesInfo.get(start)) != null){
                drawEdges(verticesInfo, start, pairS, collection);
            }
        }
    }

    private void drawEdges(Map<Integer, Pair<Double, Double>> verticesInfo, Integer start, Pair<Double, Double> pairS, Collection<Pair<Integer, Double>> collection) {
        for (Pair<Integer, Double> pair : collection) {
            Integer end = pair.getKey();
            Double weight = pair.getValue();
            if(graph.shouldIgnore(start, end)) continue;

            Pair<Double, Double> pairE = verticesInfo.get(end);
            double startX = pairS.getKey();
            double startY = pairS.getValue();
            double endX = pairE.getKey();
            double endY = pairE.getValue();

            Edge edge = new Edge(startX, startY, endX, endY, start, end);
            canvasPane.getChildren().add(edge);
            if(!graph.edgeExists(end, start)){
                canvasPane.getChildren().add(edge.getArrow());
                canvasPane.getChildren().add(edge.getLabel(weight, 0));
            }else{
                canvasPane.getChildren().add(edge.getLabel(weight, graph.getWeight(end, start)));
                graph.addIgnore(end, start);
            }
            edge.setOnMouseClicked(e -> handleEdgeClick(e, edge));
        }
    }

    public void clear(){
        graph.clear();
        nodesCreated = 0;
        redraw();
    }

    public void readFromFile(File file) {
        try(Scanner sc = new Scanner(file)){
            if(!sc.hasNextLine()) throw new Exception("Wrong Format");
            String data = sc.nextLine();
            String[] tmp;
            int N = Integer.parseInt(data);

            double maxX = 1, maxY = 1;

            Pair<Double, Double>[] nodes = new Pair[N];
            Collection<Pair<Integer, Double>>[] edges = new Collection[N];
            for(int i = 0; i < N; i++){
                data = sc.nextLine();
                tmp = data.split(" ");
                double x = Double.parseDouble(tmp[0]), y = Double.parseDouble(tmp[1]);
                if(x > maxX) maxX = x;
                if(y > maxY) maxY = y;
                nodes[i] = new Pair<>(x, y);
                if(!sc.hasNextLine()) throw new Exception("Wrong Format");
            }
            for(int i = 0; i < N; i++){
                data = sc.nextLine();
                tmp = data.split(" ");
                Collection<Pair<Integer, Double>> collection = new ArrayList<>();
                for(int j = 0; j < N; j++)
                    if(i != j && Double.parseDouble(tmp[j]) > 0)
                        collection.add(new Pair<>(j+1, Double.parseDouble(tmp[j])));
                if(collection.size()>0)
                    edges[i] = collection;
                if(!sc.hasNextLine() && i < N-1) throw new Exception("Wrong Format");
            }

            double padding = 40;
            double width = canvasPane.getWidth() - 2*Node.radius - 2*padding;
            double height = canvasPane.getHeight() - 2*Node.radius - 2*padding;
            double scaleX = width/maxX;
            double scaleY = height/maxY;

            for(int i = 0; i < N; i++) {
                double x = nodes[i].getKey(), y = nodes[i].getValue();
                nodes[i] = new Pair<>(x*scaleX+Node.radius+padding, y*scaleY+Node.radius+padding);
            }

            graph.clear();
            nodesCreated = N;
            for(int i = 0; i < N; i++)
                graph.addVertex(i+1, nodes[i]);
            for(int i = 0; i < N; i++)
                graph.setEdges(i+1, edges[i]);
        }catch (IOException e){
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        redraw();
    }
}
