package com.techelevator.dao;


import com.techelevator.model.CaloriesPerDay;
import com.techelevator.model.MealSummary;

import java.util.List;

public interface ProgressDao {
    List<Double> getCaloriesByIDAndDate(Long userId, int days);
    List<Double> getCarbsByIDAndDate(Long userId, int days);
    List<Double> getProteinByIDAndDate(Long userId, int days);
    List<Double> getCholesterolByIDAndDate(Long userId, int days);
    List<Double> getSodiumByIDAndDate(Long userId, int days);
    List<Double> getFiberByIDAndDate(Long userId, int days);
    List<Double> getSugarByIDAndDate(Long userId, int days);
    List<Double> getFatByIDAndDate(Long userId, int days);
    List<CaloriesPerDay> getCaloriesByDay(CaloriesPerDay calPerDay);
    public List<MealSummary> getMealSummary(int userId);



}
