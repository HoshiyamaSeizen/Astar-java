package com.example.a_star;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Message {
    private static Text msg;
    private static boolean error = false;

    public static Text getMsg() {
        return msg;
    }

    public static void setMsg(String msg){
        setText(msg, Color.BLACK);
        Message.error = false;
    }

    public static void setError(String msg){
        setText(msg, Color.DARKRED);
        Message.error = true;
    }

    public static boolean isError(){ return error; }
    public static void clearError(){ error = false; }

    private static void setText(String msg, Color color) {
        Message.msg.setText(msg);
        Message.msg.setFill(color);
    }

    public static void setTextField(Text info) {
        Message.msg = info;
    }
}
