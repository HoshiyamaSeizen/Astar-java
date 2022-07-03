package com.example.a_star;

import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.example.a_star.Choice.*;

public class Canvas {
    private final Pane canvasPane;
    private final Graph graph;
    private final GraphVisuals visuals;
    private int nodesCreated;
    private Integer selectedNode;
    private final double padding = 50;

    public Canvas(Pane pane){
        this.canvasPane = pane;
        this.graph = new Graph();
        this.visuals = new GraphVisuals();
        nodesCreated = 0;
    }

    public Graph getGraph() {
        return graph;
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
            }else System.out.println("Weight should be a positive number");
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

        try{ return dialog.showAndWait().map(Double::parseDouble).orElse(0.0); }
        catch (NumberFormatException ignored){  }
        return 0;
    }

    private void redraw(){
        canvasPane.getChildren().clear();
        visuals.clearIgnore();

        Map<Integer, Pair<Double, Double>> verticesInfo = graph.getVerticesInfo();
        Map<Integer, Collection<Pair<Integer, Double>>> edgesInfo = graph.getEdgesInfo();

        redrawEdges(edgesInfo);
        redrawVertices(verticesInfo);
    }

    private void redrawVertices(Map<Integer, Pair<Double, Double>> verticesInfo) {
        for(Integer id: verticesInfo.keySet()){
            Pair<Double, Double> pair = verticesInfo.get(id);
            Node node = new Node(id, pair.getKey(), pair.getValue());
            if(State.isAlgRunning())
                visuals.check(node);
            canvasPane.getChildren().add(node);
            canvasPane.getChildren().add(node.getLabel());
            node.setOnMouseClicked(e -> handleNodeClick(e, node));
        }
    }

    private void redrawEdges(Map<Integer, Collection<Pair<Integer, Double>>> edgesInfo) {
        for(Integer start: edgesInfo.keySet()){
            Collection<Pair<Integer, Double>> collection;
            if((collection = edgesInfo.get(start)) != null){
                drawEdges(start, collection);
            }
        }
    }

    private void drawEdges(Integer start, Collection<Pair<Integer, Double>> collection) {
        Pair<Double, Double> startPoint = graph.getVertex(start);
        for (Pair<Integer, Double> pair : collection) {
            Integer end = pair.getKey();
            Double weight = pair.getValue();
            if(visuals.shouldIgnore(start, end)) continue;

            Pair<Double, Double> endPoint = graph.getVertex(end);
            Edge edge = new Edge(
                    startPoint.getKey(), startPoint.getValue(), endPoint.getKey(), endPoint.getValue(),
                    start, end);
            if(State.isAlgRunning())
                visuals.check(edge);
            canvasPane.getChildren().add(edge);
            if(!graph.edgeExists(end, start)){
                canvasPane.getChildren().add(edge.getArrow());
                canvasPane.getChildren().add(edge.getLabel(weight, 0));
            }else{
                canvasPane.getChildren().add(edge.getLabel(weight, graph.getWeight(end, start)));
                visuals.addIgnore(end, start);
            }
            edge.setOnMouseClicked(e -> handleEdgeClick(e, edge));
        }
    }

    public void clear(){
        graph.clear();
        nodesCreated = 0;
        redraw();
    }

    @SuppressWarnings("unchecked")
    public void readFromFile(File file) {
        try(Scanner sc = new Scanner(file)){
            if(!sc.hasNextLine()) throw new FileFormatException(file);
            String data = sc.nextLine();
            int N;
            try{
                if((N = Integer.parseInt(data)) < 0) throw new Exception();
            }catch (Exception e){ throw new FileFormatException(file, e); }

            Pair<Double, Double>[] nodes = new Pair[N];
            Collection<Pair<Integer, Double>>[] edges = new Collection[N];
            Pair<Double, Double> maxXY = new Pair<>(1.0, 1.0);

            maxXY = readNodesFromFile(sc, N, maxXY, nodes, file);
            readEdgesFromFile(sc, N, edges, file);
            setProperNodePositions(N, nodes, maxXY);

            graph.clear();
            nodesCreated = N;
            for(int i = 0; i < N; i++)
                graph.addVertex(i+1, nodes[i]);
            for(int i = 0; i < N; i++)
                graph.setEdges(i+1, edges[i]);
        } catch (IOException e){
            e.printStackTrace();
        } catch (FileFormatException e) {
            System.out.println(e.getMessage());
        }
        redraw();
    }

    public void writeToFile(File file){
        try (FileWriter fw = new FileWriter(file)){
            fw.write((graph.getVerticesInfo().keySet().size()) + "\n");

            for (Integer i: graph.getVerticesInfo().keySet()) {
                if (graph.vertexExists(i)){
                    fw.write((graph.getVertex(i).getKey() - padding) + " " + (graph.getVertex(i).getValue() - padding)+ "\n");
                }
            }

            for (Integer i: graph.getVerticesInfo().keySet()) {
                for (Integer j: graph.getVerticesInfo().keySet()) {
                    fw.write(graph.getWeight(i, j) +" ");
                }
                fw.write('\n');
            }
        }catch(Exception e){System.out.println(e.getMessage());}
    }

    private void setProperNodePositions(int N, Pair<Double, Double>[] nodes, Pair<Double, Double> maxXY) {
        double width = canvasPane.getWidth() - 2*Node.radius - 2*padding;
        double height = canvasPane.getHeight() - 2*Node.radius - 2*padding;
        double scaleX = width/ maxXY.getKey();
        double scaleY = height/ maxXY.getValue();

        for(int i = 0; i < N; i++) {
            double x = nodes[i].getKey(), y = nodes[i].getValue();
            nodes[i] = new Pair<>(x*scaleX+Node.radius+padding, y*scaleY+Node.radius+padding);
        }
    }

    private void readEdgesFromFile(Scanner sc, int N, Collection<Pair<Integer, Double>>[] edges, File file) throws FileFormatException {
        String[] tmp;
        for(int i = 0; i < N; i++){
            if(!sc.hasNextLine() ||
                    (tmp = sc.nextLine().split(" ")).length != N) throw new FileFormatException(file);
            Collection<Pair<Integer, Double>> collection = new ArrayList<>();
            for(int j = 0; j < N; j++) {
                double weight;
                try {
                    if((weight = Double.parseDouble(tmp[j])) < 0) throw new Exception();
                    if (i != j && weight != 0) collection.add(new Pair<>(j + 1, weight));
                }catch (Exception e){ throw new FileFormatException(file); }
            }
            if(collection.size()>0)
                edges[i] = collection;
        }
    }

    private Pair<Double, Double> readNodesFromFile(Scanner sc, int N, Pair<Double, Double> maxXY, Pair<Double, Double>[] nodes, File file) throws FileFormatException {
        String[] tmp;
        for(int i = 0; i < N; i++){
            if(!sc.hasNextLine() ||
                    (tmp = sc.nextLine().split(" ")).length != 2) throw new FileFormatException(file);
            double x, y;
            try{
                if((x = Double.parseDouble(tmp[0])) < 0 || (y = Double.parseDouble(tmp[1])) < 0) throw new Exception();
            }catch (Exception e){ throw new FileFormatException(file, e); }

            maxXY = new Pair<>(Math.max(x, maxXY.getKey()), Math.max(y, maxXY.getValue()));
            nodes[i] = new Pair<>(x, y);
        }
        return maxXY;
    }

    public void startAlg(AStar results){
        visuals.setResults(results);
        visuals.update();
        redraw();
    }

    public void algStep(){
        if(State.isAlgRunning()){
            visuals.update();
            redraw();
        }
    }

    public boolean hasMinGraph() {
        return graph.getVerticesInfo().size() > 1;
    }

    public void stopAlg() {
        visuals.clear();
        redraw();
    }
}
