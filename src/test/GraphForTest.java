import com.example.a_star.Choice;
import com.example.a_star.Graph;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class GraphForTest extends Graph {
    private Integer start;
    private Integer end;
    private Choice.HEURISTIC heur;

    public GraphForTest(File file) {
        super();
        try(Scanner sc = new Scanner(file)){
            String data = sc.nextLine();
            int N = Integer.parseInt(data);

            //Pair<Double, Double>[] nodes = new Pair[N];
            //Collection<Pair<Integer, Double>>[] edges = new Collection[N];

            String[] tmp;
            for(int i = 0; i < N; i++){
                tmp = sc.nextLine().split(" ");
                double x = Double.parseDouble(tmp[0]);
                double y = Double.parseDouble(tmp[1]);
                this.addVertex(i+1, new Pair<>(x, y));
            }

            for(int i = 0; i < N; i++){
                tmp = sc.nextLine().split(" ");
                Collection<Pair<Integer, Double>> collection = new ArrayList<>();
                for(int j = 0; j < N; j++) {
                    double weight = Double.parseDouble(tmp[j]);
                    if (i != j && weight != 0) collection.add(new Pair<>(j + 1, weight));
                }
                if(collection.size()>0)
                    this.setEdges(i+1, collection);
            }

            //считывание начальной вершины
            data = sc.nextLine();
            start = Integer.parseInt(data);

            //считывание конечной вершины
            data = sc.nextLine();
            end = Integer.parseInt(data);

            //считывание эвристики
            heur = Choice.HEURISTIC.values()[(sc.nextInt())];

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Integer getStart() { return start; }
    public Integer getEnd() { return end; }
    public Choice.HEURISTIC getHeur() { return  heur; }
}

