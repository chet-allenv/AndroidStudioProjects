package edu.uncc.assessment04.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "toDoListItems")
public class ToDoListItem {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo
    String name;

    @ColumnInfo
    String priority;

    @ColumnInfo
    long toDoListId;

    @ColumnInfo
    long userId;

    public ToDoListItem() {
   }

    public ToDoListItem(String name, String priority, long toDoListId, long userId) {
        this.name = name;
        this.priority = priority;
        this.toDoListId = toDoListId;
        this.userId = userId;
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

    public long getId() {
        return id;
    }

    public long getToDoListId() {
        return toDoListId;
    }

    public void setToDoListId(long toDoListId) {
        this.toDoListId = toDoListId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
