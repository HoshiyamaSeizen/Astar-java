package com.example.a_star;

import java.io.File;

public class FileFormatException extends Exception {
    public FileFormatException(){
        super();
    }

    public FileFormatException(File file){
        super("Неверный формат файла: " + file.getName());
    }

    public FileFormatException(File file, Throwable t){
        super("Неверный формат файла: " + file.getName(), t);
    }
}
