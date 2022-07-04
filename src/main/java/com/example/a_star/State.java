package com.example.a_star;

public class State {
    private static boolean algIsRunning = false;
    private static boolean algIsPaused = true;
    private static boolean algFinished = false;
    private static int step = 0;

    public static boolean isAlgRunning() {
        return algIsRunning;
    }

    public static boolean isAlgPaused() {
        return algIsPaused;
    }

    public static int getStep() {
        return step;
    }

    public static void setAlgRun(boolean algIsRunning) {
        State.algIsRunning = algIsRunning;
    }

    public static void toggleAlgPause() {
        algIsPaused = !algIsPaused;
    }

    public static void setStep(int step) {
        State.step = step;
    }

    public static void next(){
        step++;
    }

    public static void prev(){
        step = Math.max(0, --step);
    }

    public static boolean isAlgFinished() {
        return algFinished;
    }

    public static void setAlgFinished(boolean algFinished) {
        State.algFinished = algFinished;
    }

    public static void setAlgPaused(boolean paused) {
        algIsPaused = paused;
    }
}
