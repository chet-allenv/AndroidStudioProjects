package edu.uncc.assignment11.models;

public class ToDoListItem {
    String name;
    String priority;

    public ToDoListItem() {
    }

    public ToDoListItem(String name, String priority) {
        this.name = name;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
