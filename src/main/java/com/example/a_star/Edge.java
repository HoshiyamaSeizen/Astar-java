package com.example.a_star;

import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;

public class Edge extends Line {
    private final static int arrowSize = 11;
    private final int startID;
    private final int endID;
    private double startX, startY, endX, endY;
    private final Path arrow;
    private final Label label;

    Edge(double startX, double startY, double endX, double endY, int start, int end){
        super(startX, startY, endX, endY);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.startID = start;
        this.endID = end;
        this.arrow = new Path();
        this.label = new Label();
    }

    public Path getArrow(){
        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        endX += sin*Node.radius;
        endY -= cos*Node.radius;
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowSize + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowSize + endY;
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowSize + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowSize + endY;

        arrow.getElements().add(new MoveTo(endX, endY));
        arrow.getElements().add(new LineTo(x1, y1));
        arrow.getElements().add(new MoveTo(endX, endY));
        arrow.getElements().add(new LineTo(x2, y2));

        return arrow;
    }

    public Label getLabel(double weightForward, double weightBackward){
        double angle = Math.toDegrees(Math.atan2((endY - startY), (endX - startX)));

        String weight = (weightBackward != 0 && weightForward != weightBackward) ?
                "< " + format(weightBackward) + " | " + format(weightForward) + " >" : format(weightForward);
        Text text = new Text(weight);

        label.setText(weight);
        label.setLayoutX(Math.min(startX, endX)+Math.abs(startX-endX)/2-text.getLayoutBounds().getWidth()/2);
        label.setLayoutY(Math.min(startY,endY)+Math.abs(startY-endY)/2-text.getLayoutBounds().getHeight()/2);
        label.setStyle("-fx-background-color: #fff; -fx-rotate: " + angle);
        return label;
    }

    public int getStartID() {
        return startID;
    }

    public int getEndID() {
        return endID;
    }

    private static String format(double val) {
        if(val == (long)val)
            return String.format("%d", (long) val);
        else
            return String.format("%s", val);
    }

    public void setEdgeStyle(String style){
        this.setStyle(style);
        if(arrow != null) arrow.setStyle(style);
    }
}

