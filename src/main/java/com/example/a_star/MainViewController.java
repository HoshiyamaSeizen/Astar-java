package com.example.a_star;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.a_star.Choice.*;

public class MainViewController implements Initializable {
    Canvas canvas;

    @FXML
    private Pane canvasPane;
    @FXML
    private ChoiceBox<Pair<ACTION, String>> actions;
    @FXML
    private  ChoiceBox<Pair<HEURISTIC, String>> heuristics;
    @FXML
    private MenuItem readFileButton;
    @FXML
    private MenuItem saveFileButton;
    @FXML
    private Button runAlgButton;
    @FXML
    private Button endAlgButton;
    @FXML
    private Button prevStepButton;
    @FXML
    private Button pauseAlgButton;
    @FXML
    private Button nextStepButton;
    @FXML
    private Text info;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        canvas = new Canvas(canvasPane);
        Message.setTextField(info);
        actions.setConverter(createStringConverter(actions));
        actions.getSelectionModel().selectedItemProperty().addListener(createChangeListener(ACTION.NONE));
        actionsLoadData();
        heuristics.setConverter(createStringConverter(heuristics));
        heuristics.getSelectionModel().selectedItemProperty().addListener(createChangeListener(HEURISTIC.NONE));
        heuristicLoadData();
        Message.setMsg("Создайте граф при помощи инструментов или импортируйте из файла");
    }

    @FXML
    private void run(){
        Graph graph = canvas.getGraph();
        Pair<Integer, Integer> pair = getStartEndNodes(graph);
        if(pair != null){
            setDisableRunningButtons(false);
            AStar alg = new AStar(graph, pair.getKey(), pair.getValue(), getHeuristic());
            canvas.startAlg(alg);
            Message.setMsg("Начало алгоритма");
        }
    }

    @FXML
    private void endAlg() {
        setDisableRunningButtons(true);
        State.setStep(0);
        State.setAlgFinished(true);
        canvas.stopAlg();
        Message.setMsg("Алгоритм остановлен");
    }

    private void setDisableRunningButtons(boolean disableOrNot) {
        State.setAlgRun(!disableOrNot);
        State.setAlgPaused(disableOrNot);
        State.setAlgFinished(disableOrNot);
        runAlgButton.setDisable(!disableOrNot);
        endAlgButton.setDisable(disableOrNot);
        actions.setDisable(!disableOrNot);
        heuristics.setDisable(!disableOrNot);
        checkPrevPauseNextButtons();
    }

    private void checkPrevPauseNextButtons(){
        nextStepButton.setDisable(State.isAlgFinished() || !State.isAlgPaused() || !State.isAlgRunning());
        prevStepButton.setDisable(State.getStep() == 0 || !State.isAlgPaused() || !State.isAlgRunning());
        pauseAlgButton.setDisable(State.isAlgFinished() || !State.isAlgRunning());
        pauseAlgButton.setText(State.isAlgPaused() ? "Resume" : "Pause");
    }

    @FXML
    public void nextStep(){
        State.next();
        canvas.algStep();
        checkPrevPauseNextButtons();
    }

    @FXML
    public void prevStep(){
        State.prev();
        canvas.algStep();
        checkPrevPauseNextButtons();
    }

    @FXML
    public void pauseAlg(){
        State.toggleAlgPause();
        checkPrevPauseNextButtons();
    }

    @FXML
    private void chooseFile(ActionEvent e) {
        if(!State.isAlgRunning()) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            MenuItem node = (MenuItem) e.getSource();
            File file; boolean res;
            if (node.equals(readFileButton)
                    && (file = fileChooser.showOpenDialog(new Stage())) != null) {
                res = canvas.readFromFile(file);
                checkRunAvailability();
                if(res) chooseMsg("Граф загружен.");
            } else if (node.equals(saveFileButton)
                    && (file = fileChooser.showSaveDialog(new Stage())) != null) {
                res = canvas.writeToFile(file);
                if(res) Message.setMsg("Граф сохранён");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void heuristicLoadData() {
        heuristics.setValue(new Pair<>(HEURISTIC.NONE, "Выберите эвристику..."));
        heuristics.setItems(FXCollections.observableArrayList(
                new Pair<>(HEURISTIC.CHEBYSHEV, "Расстояние Чебышева"),
                new Pair<>(HEURISTIC.MANHATTAN, "Манхэттенская метрика"),
                new Pair<>(HEURISTIC.EUCLID, "Евклидово расстояние"),
                new Pair<>(HEURISTIC.DIJKSTRA, "Нулевая эвристика (Дейкстры)")
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
            if(t instanceof ACTION) {
                setAction((ACTION) selected.getValue().getKey());
                chooseMsgAction();
            }
            else if(t instanceof HEURISTIC) {
                setHeuristic((HEURISTIC) selected.getValue().getKey());
                checkRunAvailability();
                chooseMsg(null);
            }
        };
    }

    @FXML
    private void onCanvasClick(MouseEvent e) {
        if (e.getSource().equals(canvasPane)) {
            if (e.getButton() == MouseButton.PRIMARY && getAction() == ACTION.ADD) {
                canvas.addNode(e.getX(), e.getY());
            }
        }
        checkRunAvailability();

        if(!Message.isError()){
            if(getAction()!=ACTION.ADD) chooseMsgAction();
            else chooseMsg(null);
        }
        else Message.clearError();
    }

    private void checkRunAvailability(){
        runAlgButton.setDisable(Choice.checkHeuristic(HEURISTIC.NONE) || !canvas.hasMinGraph() || State.isAlgRunning());
    }

    private Pair<Integer, Integer> getStartEndNodes(Graph graph) {
        Integer start = requestNode(1, true);
        Integer end = requestNode(2, false);
        if(graph.getVertex(start) == null || graph.getVertex(end) == null){
            Message.setError("Пара указанных вершин не существует");
            return null;
        }
        return new Pair<>(start, end);
    }

    private int requestNode(Integer defaultNode, boolean startNode){
        TextInputDialog dialog = new TextInputDialog(String.valueOf(defaultNode));
        dialog.setTitle(null);
        dialog.setContentText(null);
        dialog.setHeaderText("Введите " + (startNode?"стартовую":"конечную") + " вершину:");

        try{ return dialog.showAndWait().map(Integer::parseUnsignedInt).orElse(0); }
        catch (NumberFormatException ignored){  }
        return 0;
    }

    private void chooseMsg(String prev) {
        if(prev != null && !prev.equals("")) prev += " ";
        else prev = "";

        if(canvas.hasMinGraph()){
            Message.setMsg(prev + (
                    !runAlgButton.isDisabled() ? "Нажмите 'run' для старта алгоритма" : "Выберете эвристику"));
        }
        else Message.setMsg(prev + "В графе должно быть минимум две вершины");
    }

    private void chooseMsgAction(){
        switch (getAction()){
            case ADD -> Message.setMsg("Кликните по полотну, чтобы создать вершину");
            case CONNECT -> Message.setMsg("Кликните по двум вершинам, чтобы соединить их");
            case DELETE -> Message.setMsg("Кликните по вершине или ребру, чтобы удалить объект");
        }
    }

    @FXML
    private void clear(){
        if(!State.isAlgRunning()) {
            canvas.clear();
            checkRunAvailability();
            Message.setMsg("Создайте граф при помощи инструментов или импортируйте из файла");
        }
    }
}