package com.example.a_star;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class Node extends StackPane {
    public final static double radius = 15;
    private final int ID;
    private final Label label;

    Node(int id, double x, double y){
        super();
        this.setLayoutX(x-radius);
        this.setLayoutY(y-radius);
        Circle circle = new Circle(x, y, radius);
        circle.setFill(Color.WHEAT);
        Text text = new Text(String.valueOf(id));
        text.setBoundsType(TextBoundsType.VISUAL);
        this.getChildren().addAll(circle, text);
        label = new Label();
        label.setLayoutX(x+radius+4);
        label.setLayoutY(y-radius-4);
        label.setStyle("-fx-text-fill: #7a5109;");
        this.ID = id;
    }

    public void setLabel(double f, double h){
        label.setText("h: " + Math.round(h * 100.0) / 100.0 + "\nf: " + Math.round(f * 100.0) / 100.0);
    }

    public Label getLabel(){ return label; }

    public int id(){ return ID; }
}

