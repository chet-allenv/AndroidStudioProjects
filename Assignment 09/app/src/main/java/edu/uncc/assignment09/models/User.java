package edu.uncc.assignment09.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users")
public class User implements Serializable {


    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo
    private String name;
    @ColumnInfo
    private int age;
    @ColumnInfo
    private int creditScore;
    @ColumnInfo
    private String state;

    public User() {
    }

    public User(String name, int age, int creditScore, String state) {
        this.name = name;
        this.age = age;
        this.creditScore = creditScore;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
