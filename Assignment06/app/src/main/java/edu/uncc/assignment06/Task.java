package edu.uncc.assignment06;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable{
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    String name;
    String date;
    Priority priority;

    public Task(String name, String date, Priority priority)
    {
        this.name = name;
        this.date = date;
        this.priority = priority;
    }
}
