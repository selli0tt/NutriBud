package com.techelevator.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Meal {

    private int userId;
    private long mealId;
    private String mealName;
    private LocalDate mealDate;
    private boolean isSaved;

    public Meal() {
    }

    public Meal(String mealName, LocalDate mealDate, boolean isSaved){
        this.mealName = mealName;
        this.mealDate = mealDate;
        this.isSaved = isSaved;
    }

    public Meal( long mealId, int userId, String mealName, LocalDate mealDate, boolean isSaved) {
        this.mealId = mealId;
        this.userId = userId;
        this.mealName = mealName;
        this.mealDate = mealDate;
        this.isSaved = isSaved;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getMealId() {
        return mealId;
    }

    public void setMealId(long mealId) {
        this.mealId = mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public LocalDate getMealDate() {
        return mealDate;
    }

    public void setMealDate(LocalDate mealDate) {
        this.mealDate = mealDate;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }
}
