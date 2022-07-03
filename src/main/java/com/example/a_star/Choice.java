package com.example.a_star;

public class Choice {
    public enum ACTION { NONE, ADD, CONNECT, DELETE }
    public enum HEURISTIC { NONE, CHEBYSHEV, MANHATTAN, EUCLID }

    private static ACTION chosenAction = ACTION.NONE;
    private static HEURISTIC chosenHeuristic = HEURISTIC.NONE;

    public static ACTION getAction() {
        return chosenAction;
    }

    public static void setAction(ACTION action) {
        chosenAction = action;
    }

    public static boolean checkAction(ACTION action){
        return chosenAction == action;
    }

    public static HEURISTIC getHeuristic() {
        return chosenHeuristic;
    }

    public static void setHeuristic(HEURISTIC heuristic) {
        chosenHeuristic = heuristic;
    }

    public static boolean checkHeuristic(HEURISTIC heuristic){
        return chosenHeuristic == heuristic;
    }
}
