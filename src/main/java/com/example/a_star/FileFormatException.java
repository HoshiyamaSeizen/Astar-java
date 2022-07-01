package com.example.a_star;

import java.io.File;

public class FileFormatException extends Exception {
    public FileFormatException(){
        super();
    }

    public FileFormatException(File file){
        super("Wrong format of the file: " + file.getAbsolutePath());
    }

    public FileFormatException(File file, Throwable t){
        super("Wrong format of the file: " + file.getAbsolutePath(), t);
    }
}
