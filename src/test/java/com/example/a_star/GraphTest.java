package com.example.a_star;

import junit.framework.TestCase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

public class GraphTest extends TestCase {

    static ArrayList<GraphForTest> graphList = new ArrayList<>();
    static int COUNT_TEST = 4;

    @BeforeAll
    public static void readGraphs() {
        GraphForTest graph;
        for (int i = 1; i <= COUNT_TEST; i++) {
            graph = new GraphForTest(new File(String.format("./src/test/java/com/example/a_star/graphs/%s.txt", i)));
            graphList.add(graph);
        }
    }
    @AfterAll
    public static void clean() {
        graphList = new ArrayList<>();
    }

    @Test
    public void testEdgesInfo() {
        int i = 0;
        ArrayList<String> expectedEdgesInfo = new ArrayList<>();
        expectedEdgesInfo.add("{1=[2=3.0, 3=7.0], 2=[3=5.0], 3=[4=1.0]}");
        expectedEdgesInfo.add("{1=[2=2.0, 4=7.0, 5=6.0], 2=[3=2.0], 4=[9=10.0], 5=[6=5.0], 6=[7=3.0], 7=[8=8.0], 8=[9=5.0]}");
        expectedEdgesInfo.add("{1=[2=2.0], 2=[3=2.0], 3=[1=2.0]}");
        expectedEdgesInfo.add("{2=[1=5.0, 4=8.0], 4=[5=7.0], 5=[2=4.0, 3=10.0]}");

        for (GraphForTest graph : graphList) {
            String actual = graph.getEdgesInfo().toString();
            Assertions.assertEquals(expectedEdgesInfo.get(i++), actual,String.format("Test #%s failed.", i)+" EdgesInfo");
        }
    }

    @Test
    public void testVerticesInfo() {
        int i = 0;
        ArrayList<String> expectedVerticesInfo = new ArrayList<>();
        expectedVerticesInfo.add("{1=479.0=483.0, 2=305.0=307.5, 3=595.0=190.5, 4=885.0=424.5}");
        expectedVerticesInfo.add("{1=425.0=438.0, 2=423.0=372.0, 3=420.0=311.0, 4=317.0=421.0, 5=487.0=430.0, 6=534.0=374.0, 7=526.0=319.0, 8=480.0=274.0, 9=417.0=255.0}");
        expectedVerticesInfo.add("{1=418.0=464.0, 2=383.0=382.0, 3=331.0=443.0, 4=416.0=235.0}");
        expectedVerticesInfo.add("{1=410.0=454.0, 2=350.0=354.0, 3=453.0=354.0, 4=487.0=256.0, 5=311.0=250.0}");

        for (GraphForTest graph : graphList) {
            String actual = graph.getVerticesInfo().toString();
            Assertions.assertEquals(expectedVerticesInfo.get(i++), actual, String.format("Test #%s failed.", i)+" VerticesInfo");
        }
    }
}

