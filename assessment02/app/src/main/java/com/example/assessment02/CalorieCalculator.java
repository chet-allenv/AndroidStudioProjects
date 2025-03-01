package com.example.assessment02;

import java.io.Serializable;

public class CalorieCalculator implements Serializable {
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Integer getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getHeightString() {
        return heightFeet + "'" + heightInches + "\"";
    }

    public String getActivityLevelString()
    {
        if (activityLevel == null)
        {
            return null;
        }
        switch (activityLevel)
        {
            case 0:
                return "Sedentary";
            case 1:
                return "Lightly Active";
            case 2:
                return "Moderately Active";
            case 3:
                return "Very Active";
            case 4:
                return "Super Active";
            default:
                return null;
        }
    }

    private String gender;
    private Integer weight;
    private Integer heightFeet;
    private Integer heightInches;
    private Integer age;
    private Integer activityLevel; // 0-4

    public CalorieCalculator()
    {}


    public Integer getHeightFeet() {
        return heightFeet;
    }

    public void setHeightFeet(Integer heightFeet) {
        this.heightFeet = heightFeet;
    }

    public Integer getHeightInches() {
        return heightInches;
    }

    public void setHeightInches(Integer heightInches) {
        this.heightInches = heightInches;
    }
}
