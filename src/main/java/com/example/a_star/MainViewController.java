package com.example.a_star;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    ObservableList list = FXCollections.observableArrayList();

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ChoiceBox<String> actions;

    @FXML
    private  ChoiceBox<String> heuristics;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        System.out.println("Init");
        actionsLoadData();
        heuriscticLoadData();
    }

    @FXML
    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(new Stage());

        System.out.println(file);
    }

    private void heuriscticLoadData() {
        heuristics.setValue("Выберите эвристику");
        list.removeAll(list); // Что бы не случалось повторения
        list.addAll(
                "Расстояеие Чебышева",
                "Манхэттенская метрика",
                "Евклидово расстояние"
        );
        heuristics.getItems().addAll(list);
    }
    private void actionsLoadData() {
        actions.setValue("Выберите действие");
        list.removeAll(list); // Что бы не случалось повторения
        list.addAll(
                "Создать вершину",
                "Соединить вершины",
                "Удалить вершину"
        );
        actions.getItems().addAll(list);
    }


    @FXML
    public void openAboutWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("about-view.fxml"));
            /*
             * if "fx:controller" is not set in fxml
             * fxmlLoader.setController(NewWindowController);
             */
            Scene scene = new Scene(fxmlLoader.load(), 400, 400);
            Stage stage = new Stage();
            stage.setTitle("New Window");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("exception in opening about window" + e);
        }
    }

}