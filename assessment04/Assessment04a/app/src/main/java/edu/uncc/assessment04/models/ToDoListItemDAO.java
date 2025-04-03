package edu.uncc.assessment04.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ToDoListItemDAO {
    @Insert
    void insertAll(ToDoListItem... toDoListItems);

    @Delete
    void delete(ToDoListItem toDoListItem);

    @Query("SELECT * FROM toDoListItems")
    List<ToDoListItem> getAll();

    @Query("SELECT * FROM toDoListItems WHERE id = :id")
    ToDoListItem getItemById(int id);

    @Query("SELECT * FROM toDoListItems WHERE toDoListId = :toDoListId")
    List<ToDoListItem> getItemsByToDoListId(long toDoListId);

    @Update
    void updateItem(ToDoListItem toDoListItem);
}
