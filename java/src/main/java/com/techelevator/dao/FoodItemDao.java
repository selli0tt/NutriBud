package com.techelevator.dao;

import com.techelevator.model.FoodItem;
import com.techelevator.model.Meal;

import java.time.LocalDate;
import java.util.List;

public interface FoodItemDao {
    List<FoodItem> searchFood(String searchTerm);

    FoodItem getFoodNutrition(int fdcId);

    List<FoodItem> getQuickFoods(long userId);

    List<FoodItem> getAllFoodFromMeal(Meal meal);

    void deleteFood( String mealName,  LocalDate mealDate, int[] foodId, long mealId);

}
