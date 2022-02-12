package com.techelevator.dao;

import com.techelevator.model.FoodItem;
import com.techelevator.model.Meal;
import com.techelevator.model.User;
import com.techelevator.model.UserProfile;

import java.time.LocalDate;
import java.util.List;

public interface MealDao {

    int create(Meal meal);

    Meal getMealById(long mealId);

    Meal getLastMeal(int userId);

    List<Meal> getAllMeals(long userId);

    void deleteMeal(long mealId);

    List<Meal> getQuickMeals(long userId);

    int getMealId(Meal meal);

    void addMealItem(List<FoodItem> items, int mealId);

    String getDateByMeal(long mealId);

    List<Meal> getMealsByFoodIdAndMealId(long foodId, long mealId);

    int trackMealsForStars(LocalDate mealDate, int userId);


}
