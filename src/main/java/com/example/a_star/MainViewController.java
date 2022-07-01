package com.example.a_star;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static com.example.a_star.Choice.*;

public class MainViewController implements Initializable {
    Canvas canvas;
    @FXML
    private Pane canvasPane;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ChoiceBox<Pair<ACTION, String>> actions;
    @FXML
    private  ChoiceBox<Pair<HEURISTIC, String>> heuristics;
    @FXML
    private Text info;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        actions.setConverter(createStringConverter(actions));
        actions.getSelectionModel().selectedItemProperty().addListener(createChangeListener(ACTION.NONE));
        actionsLoadData();
        heuristics.setConverter(createStringConverter(heuristics));
        heuristics.getSelectionModel().selectedItemProperty().addListener(createChangeListener(HEURISTIC.NONE));
        heuristicLoadData();
        canvas = new Canvas(canvasPane);
    }

    @FXML
    private void chooseFile(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(new Stage());
        if(file == null) return;
        MenuItem node = (MenuItem) e.getSource();
        if(node.getId().equals("fileChooseButton")) canvas.readFromFile(file);
        else canvas.saveToFile(file);
    }

    @SuppressWarnings("unchecked")
    private void heuristicLoadData() {
        heuristics.setValue(new Pair<>(HEURISTIC.NONE, "Выберите эвристику..."));
        heuristics.setItems(FXCollections.observableArrayList(
                new Pair<>(HEURISTIC.CHEBYSHEV, "Расстояние Чебышева"),
                new Pair<>(HEURISTIC.MANHATTAN, "Манхэттенская метрика"),
                new Pair<>(HEURISTIC.EUCLID, "Евклидово расстояние")
        ));
    }
    @SuppressWarnings("unchecked")
    private void actionsLoadData() {
        actions.setValue(new Pair<>(ACTION.NONE, "Выберите действие..."));
        actions.setItems(FXCollections.observableArrayList(
                new Pair<>(ACTION.ADD, "Создать вершину"),
                new Pair<>(ACTION.CONNECT, "Соединить вершины"),
                new Pair<>(ACTION.DELETE, "Удалить вершину/ребро")
        ));
    }

    @FXML
    public void openAboutWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("about-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 450, 500);
            Stage stage = new Stage();
            stage.setTitle("About");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Error while opening the 'about' window");
            e.printStackTrace();
        }
    }

    private <T> StringConverter<Pair<T, String>> createStringConverter(ChoiceBox<Pair<T, String>> choiceBox){
        return new StringConverter<>() {
            @Override
            public String toString(Pair<T, String> pair) { return pair.getValue(); }
            @Override
            public Pair<T, String> fromString(String s) {
                return choiceBox.getItems().stream().filter(item ->
                        item.getValue().equals(s)).findFirst().orElse(null);
            }
        };
    }

    private <T> ChangeListener<Pair<T, String>> createChangeListener(T t){
        return (selected, t1, t2) -> {
            if(t instanceof ACTION) setAction((ACTION) selected.getValue().getKey());
            else if(t instanceof HEURISTIC) setHeuristic((HEURISTIC) selected.getValue().getKey());
        };
    }

    @FXML
    private void onCanvasClick(MouseEvent e) {
        if (e.getSource().equals(canvasPane)) {
            if (e.getButton() == MouseButton.PRIMARY && getAction() == ACTION.ADD) {
                canvas.addNode(e.getX(), e.getY());
            }
        }
    }

    @FXML
    private void clear(){
        canvas.clear();
    }
}