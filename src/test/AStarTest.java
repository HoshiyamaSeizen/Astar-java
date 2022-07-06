import com.example.a_star.AStar;
import junit.framework.TestCase;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.*;

public class AStarTest extends TestCase {

    static ArrayList<GraphForTest> graphList = new ArrayList<>();
    static ArrayList<AStar> resList = new ArrayList<>();
    static int COUNT_TEST = 4;

    @BeforeAll
    public static void readGraphs() {
        GraphForTest graph;
        for (int i = 1; i <= COUNT_TEST; i++) {
            graph = new GraphForTest(new File(String.format("./src/test/graphs/%s.txt", i)));
            graphList.add(graph);
        }
        AStar alg;
        for (GraphForTest gr : graphList) {
            alg = new AStar(gr, gr.getStart(), gr.getEnd(), gr.getHeur());
            resList.add(alg);
        }
    }
    @AfterAll
    public static void clean() {
        graphList = new ArrayList<>();
        resList = new ArrayList<>();
    }
    @Test
    public void testEdgesSteps() {
        int i = 0;
        ArrayList<String> expectedEdgeSteps = new ArrayList<>();
        expectedEdgeSteps.add("[1=3, 3=4]");
        expectedEdgeSteps.add("[1=2, 2=3, 1=5, 5=6, 6=7, 7=8, 8=9]");
        expectedEdgeSteps.add("[1=2, 2=3]");
        expectedEdgeSteps.add("[]");

        for (AStar graph : resList) {
            String str = graph.getEdgeSteps().toString();
            Assertions.assertEquals(expectedEdgeSteps.get(i++), str, String.format("Test #%s failed.", i)+" EdgesSteps");
        }
    }

    @Test
    public void testFinalPath() {
        int i = 0;
        ArrayList<String> expectedFinalPath = new ArrayList<>();
        expectedFinalPath.add("[1=3, 3=4]");
        expectedFinalPath.add("[1=5, 5=6, 6=7, 7=8, 8=9]");
        expectedFinalPath.add("[]");
        expectedFinalPath.add("[]");

        for (AStar graph : resList) {
            String str = graph.getFinalPath().toString();
            Assertions.assertEquals(expectedFinalPath.get(i++), str,String.format("Test #%s failed.", i)+" FinalPath");
        }
    }

    @Test
    public void testCountSteps() {
        int i = 0;
        ArrayList<Integer> expectedCountSteps = new ArrayList<>();
        expectedCountSteps.add(2);
        expectedCountSteps.add(7);
        expectedCountSteps.add(2);
        expectedCountSteps.add(0);

        for (AStar graph : resList) {
            Integer n = graph.getCountSteps();
            Assertions.assertEquals(expectedCountSteps.get(i++), n, String.format("Test #%s failed.", i)+" CountSteps");
        }
    }

    @Test
    public void testPathLen() {
        int i = 0;
        ArrayList<Double> expectedPathLen = new ArrayList<>();
        expectedPathLen.add(8.0);
        expectedPathLen.add(27.0);
        expectedPathLen.add(0.0);
        expectedPathLen.add(0.0);

        for (AStar graph : resList) {
            Double n = graph.getPathLen();
            Assertions.assertEquals(expectedPathLen.get(i++), n, String.format("Test #%s failed.", i)+" PathLen");
        }
    }

    @Test
    public void testHeuristics() {
        int i = 0;
        ArrayList<String> expectedHeuristics = new ArrayList<>();
        expectedHeuristics.add("[{2=583.0=580.0, 3=297.0=290.0}, {4=8.0=0.0}]");
        expectedHeuristics.add("[{2=125.0=123.0, 4=273.0=266.0, 5=251.0=245.0}, {3=63.0=59.0}, {}, {6=247.0=236.0}, {7=187.0=173.0}, {8=104.0=82.0}, {9=27.0=0.0}]");
        expectedHeuristics.add("[{2=152.65855435387664=150.65855435387664}, {3=228.69757453074567=224.69757453074567}, {}]");
        expectedHeuristics.add("[{}]");

        for (AStar graph : resList) {
            String str = graph.getHeuristicSteps().toString();
            Assertions.assertEquals(expectedHeuristics.get(i++), str,  String.format("Test #%s failed.", i)+" HeuristicSteps");
        }
    }
}


