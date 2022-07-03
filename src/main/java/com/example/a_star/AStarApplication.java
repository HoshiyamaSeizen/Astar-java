package com.example.a_star;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class AStarApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AStarApplication.class.getResource("main-view.fxml"));
        algAsyncRunner(fxmlLoader);
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("A* Algorithm Visualizer");
        stage.setScene(scene);
        stage.show();
        ((MainViewController) fxmlLoader.getController()).openAboutWindow();
    }

    private void algAsyncRunner(FXMLLoader fxmlLoader) {
        Timeline timer = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                MainViewController mainViewController = fxmlLoader.getController();
                if (!State.isAlgPaused()) mainViewController.nextStep();
            }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public static void main(String[] args) { launch(); }
}