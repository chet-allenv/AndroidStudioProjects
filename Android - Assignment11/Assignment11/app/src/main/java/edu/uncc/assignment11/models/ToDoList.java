package edu.uncc.assignment11.models;

import java.io.Serializable;

public class ToDoList implements Serializable {
    String name;

    int toDoListId;

    public ToDoList() {
    }

    public ToDoList(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getToDoListId() { return toDoListId;}
    public void setToDoListId(int toDoListId) {this.toDoListId = toDoListId;}
}
