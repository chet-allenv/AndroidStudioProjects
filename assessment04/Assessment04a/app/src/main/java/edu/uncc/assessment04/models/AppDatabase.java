package edu.uncc.assessment04.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, ToDoList.class, ToDoListItem.class}, version = 7)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDAO userDao();
    public abstract ToDoListDAO toDoListDao();
    public abstract ToDoListItemDAO toDoListItemDao();
}
