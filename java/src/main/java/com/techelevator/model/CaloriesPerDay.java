package com.techelevator.model;

import java.time.LocalDate;

public class CaloriesPerDay {

    int sum;
    LocalDate calorieDate;
    long userId;
    LocalDate startDate;
    LocalDate endDate;

    public CaloriesPerDay() {}

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public LocalDate getCalorieDate() {
        return calorieDate;
    }

    public void setCalorieDate(LocalDate calorieDate) {
        this.calorieDate = calorieDate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "CaloriesPerDay{" +
                "sum=" + sum +
                ", calorieDate=" + calorieDate +
                ", userId=" + userId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
