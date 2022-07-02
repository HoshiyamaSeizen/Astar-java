package com.example.a_star;

import java.util.*;
import javafx.util.Pair;
import static java.lang.Math.*;

public class AStar {
    private ArrayList<Map<Integer, Pair<Double,Double>>> heuristicsSteps;  //список h(x) и f(x) рассматриваемых вершин на каждом шаге
    private ArrayList<Pair<Integer, Integer>> edgesSteps;        //ребра, которые должны подкрашиваться на каждом шаге
    private Double pathLen;                                                 //длина найденного кратчайшего пути
    private Integer countSteps;                                             //количество шагов
    private ArrayList<Pair<Integer, Integer>> finalPath;                    //итоговый кратч.путь - н-р, список ребер [(1,2),(2,3),(3,4)]

    public Double computeHeuristic(Choice.HEURISTIC heur, Pair <Double, Double> coordsCur, Pair <Double, Double> coordsEnd) {
        Double h = 0.0;
        try {
            switch (heur) {
                case EUCLID:
                    h = Math.sqrt(pow((coordsCur.getKey() - coordsEnd.getKey()), 2) + pow((coordsCur.getValue() - coordsEnd.getValue()), 2));
                    break;
                case CHEBYSHEV:
                    h = max(abs(coordsCur.getKey() - coordsEnd.getKey()), abs(coordsCur.getValue() - coordsEnd.getValue()));
                    break;
                case MANHATTAN:
                    h = abs(coordsCur.getKey() - coordsEnd.getKey()) + abs(coordsCur.getValue() - coordsEnd.getValue());
                    break;
                default: //!!! в случае если пользователь не выбрал эвристику (надо как-то обработать либо здесь, либо в графике)
                    throw new IllegalArgumentException("Exception: Вы не выбрали эвристику");
            };
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        return h;
    }

    public Pair<ArrayList<Pair<Integer, Integer>>, Double> buildPath(Map<Integer, Integer> map, Integer end, Graph graph) {
        ArrayList<Pair<Integer, Integer>> path = new ArrayList<>();
        Double finalWeight = 0.0;
        while (!end.equals(-1)) {
            Integer tmp = end;
            end = map.get(end);
            if (!end.equals(-1))
                path.add(new Pair<>(end, tmp));
            finalWeight += graph.getWeight(end, tmp);
        }
        ArrayList<Pair<Integer, Integer>> reversePath = new ArrayList<Pair<Integer, Integer>>();
        for (int i = path.size() - 1; i >= 0; i--) {

            // Append the elements in reverse order
            reversePath.add(path.get(i));
        }
        return new Pair<>(reversePath,finalWeight);

    }

    public AStar(Graph graph, Integer start, Integer end, Choice.HEURISTIC heur) {
        heuristicsSteps = new ArrayList<>();
        edgesSteps = new ArrayList<>();
        pathLen = 0.0;
        countSteps = -1; //т.к. в цикле while извлечение стартовой вершины из кучи тоже считается за шаг, а нам нужно учитывать только переходы по ребру

        //prepare to alg
        Map<Integer, Double> startDists = new HashMap<>();           //хранит g(x) для вершин
        startDists.put(start, 0.0);

        PriorityQueue<Pair<Pair<Integer, Integer>, Double>> heap = new PriorityQueue<>(new Comparator<Pair<Pair<Integer,Integer>, Double>>() {
            public int compare(Pair<Pair<Integer,Integer>, Double> p1, Pair<Pair<Integer, Integer>, Double> p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
        heap.add(new Pair<>(new Pair<>(start, start), 0.0));

        Map<Integer, Integer> path = new HashMap<>(); //map хранит пары: вершина - откуда в нее пришли
        path.put(start, -1);

        //start the alg
        while (!heap.isEmpty()) {
            Pair<Integer, Integer> pairCurPrev = heap.poll().getKey();
            Integer current = pairCurPrev.getKey();
            Integer prev = pairCurPrev.getValue();
            edgesSteps.add(new Pair<>(prev, current));   //запоминаем ребра, по которым гуляем
            countSteps++;

            if (current.equals(end))
                break;
            if (!graph.vertexExists(current))
                continue;
            Map<Integer, Pair<Double, Double>> adjacentHeur = new HashMap<>();                  //создаем Map эвристик смежн вершин: (смежная вершина-Pair(f(x), h(x))), где f(x)=g(x)+h(x)

            Collection<Pair<Integer, Double>> listAdj = graph.getEdgesInfo().get(current);             //коллекция смежных вершин
            if (listAdj != null){
                for (Pair <Integer, Double> pairNextWeight : listAdj) {                             //проходимся по смежным
                    Integer next = pairNextWeight.getKey();                                         //смежная вершина
                    Double weight = pairNextWeight.getValue();                                      //вес ребра до нее
                    Double g = startDists.get(current) + weight;                             //находим g(x) для смежной
                    if (!startDists.containsKey(next) || g < startDists.get(next)) {
                        path.put(next, current);
                        startDists.put(next, g); //запомнили g(x) для next
                        Double h = computeHeuristic(heur, graph.getVerticesInfo().get(next), graph.getVerticesInfo().get(end)); //найдем h(x)
                        Double f = g + h;
                        adjacentHeur.put(next, new Pair<>(f, h));
                        heap.add(new Pair<>(new Pair<>(next, current), f));
                    }
                }
            }
            heuristicsSteps.add(adjacentHeur);
        }
        edgesSteps.remove(0);
        Pair<ArrayList<Pair<Integer, Integer>>, Double> resultsAboutPath = buildPath(path, end, graph);
        finalPath = resultsAboutPath.getKey();
        pathLen =  resultsAboutPath.getValue();
    }


    public Double getPathLen() { return pathLen; }
    public Integer getCountSteps() { return countSteps; }
    public ArrayList<Pair<Integer, Integer>> getFinalPath() { return finalPath;}

    public ArrayList<Map<Integer, Pair<Double,Double>>> getHeuristicSteps() {return heuristicsSteps; }
    public ArrayList<Pair<Integer, Integer>> getEdgeSteps() {return edgesSteps; }


}
