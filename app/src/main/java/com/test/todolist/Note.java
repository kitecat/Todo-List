package com.test.todolist;

import java.io.Serializable;

/**
 * Created by FATAL1TY on 29.08.2015.
 */
public class Note implements Serializable {

    String text;
    int status;

    public Note(String _text) {
        text = _text;
        status = 0;
    }

    void setStatusDone() {
        status = 2;
    }

    void setStatusDuring() {
        status = 1;
    }

    void setText(String string) {
        text = string;
    }
}
