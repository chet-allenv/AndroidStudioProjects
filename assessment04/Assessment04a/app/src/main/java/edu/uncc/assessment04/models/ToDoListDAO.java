package edu.uncc.assessment04.models;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ToDoListDAO {

    @Insert
    void insertAll(ToDoList... toDoLists);

    @Delete
    void delete(ToDoList list);

    @Query("SELECT * FROM toDoLists")
    List<ToDoList> getAll();

    @Query("SELECT * FROM toDoLists WHERE id = :id")
    ToDoList getListById(int id);

    @Query("SELECT * FROM toDoLists WHERE userId = :userId")
    List<ToDoList> getListsByUserId(long userId);

    @Update
    void updateList(ToDoList list);
}
