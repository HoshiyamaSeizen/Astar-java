package com.example.a_star;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class Node extends StackPane {
    public final static double radius = 15;
    private final int ID;
    Node(int id_, double x, double y){
        super();
        this.setLayoutX(x-radius);
        this.setLayoutY(y-radius);
        Circle circle = new Circle(x, y, radius);
        circle.setFill(Color.WHEAT);
        Text text = new Text(String.valueOf(id_));
        text.setBoundsType(TextBoundsType.VISUAL);
        this.getChildren().addAll(circle, text);
        this.ID = id_;
    }

    public int id(){
        return ID;
    }
}

