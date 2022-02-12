package com.techelevator.controller;


import com.sun.jdi.LongValue;

import com.techelevator.dao.*;

import com.techelevator.model.*;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@CrossOrigin
public class AppController {

    @Autowired
    UserProfileDao userProfileDao;

    @Autowired
    UserDao userDao;

    @Autowired
    MealDao mealDao;

    @Autowired
    FoodItemDao foodItemDao;

    @Autowired
    ProgressDao progressDao;

    //profile methods
    @RequestMapping(path = "/createprofile", method = RequestMethod.POST)
    public UserProfile createProfile(@Valid @RequestBody UserProfile profileDao, Principal principal) {

        String name = principal.getName();

        long userId = userDao.findIdByUsername(name);
        profileDao.setUserId(userId);

        userProfileDao.create(profileDao);
        return null;
    }

    @GetMapping("/getProfile/{profileID}")
    public UserProfile getProfile(@PathVariable int profileID, Principal principal) {
        String name = principal.getName();
        int userId = userDao.findIdByUsername(name);
        return userProfileDao.getProfileById(userId);
    }

    @GetMapping("/getProfileWithoutId")
    public UserProfile getProfileWithoutId(Principal principal) {
        String name = principal.getName();
        int userId = userDao.findIdByUsername(name);
        return userProfileDao.getProfileById(userId);
    }

    @RequestMapping(path = "/editUserProfile", method = RequestMethod.PUT)
    public void editUserProfile(@Valid @RequestBody UserProfile profile, Principal principal) {
        String name = principal.getName();
        System.out.println(profile);
        long userId = userDao.findIdByUsername(name);
        profile.setUserId(userId);

        userProfileDao.editUserProfile(profile);
    }

    @RequestMapping(path = "/editPassword", method = RequestMethod.PUT)
    public void editPassword(@Valid @RequestBody String password, Principal principal) {
        String name = principal.getName();
        userDao.changePassword(name, password);
    }

    @GetMapping(path="/myProfilePic")
    public String obtainUserProfilePic(Principal principal){
        String name = principal.getName();
        long userId = userDao.findIdByUsername(name);
        return userProfileDao.gerUserProfileImage((int) userId);
    }



        //meal methods
    @RequestMapping(path="/createMeal", method = RequestMethod.POST)
    public int createMeal(@Valid @RequestBody Meal meal, Principal principal){
        String name = principal.getName();
        long userId = userDao.findIdByUsername(name);
        meal.setUserId(Math.toIntExact(userId));
        return mealDao.create(meal);
    }


    @RequestMapping(path="/useFoodIdAndDateToGetMeals/{foodId}", method = RequestMethod.POST)
    public List<Meal> mealsWithFoodIdAndDate(@Valid @RequestBody long foodId, long mealId){
        return mealDao.getMealsByFoodIdAndMealId(foodId, mealId);
    }

    @RequestMapping(path="/getLastMeal", method=RequestMethod.GET)
    public List<FoodItem> getLastMealId(@Valid Principal principal){
        String name = principal.getName();
        int userId = userDao.findIdByUsername(name);
        Meal lastMeal = mealDao.getLastMeal(userId);
        List<FoodItem> foods = foodItemDao.getAllFoodFromMeal(lastMeal);
        return foods;
    }

    @RequestMapping(path = "/getMealId", method = RequestMethod.POST)
    public int getMealIDByMeal(@Valid @RequestBody Meal meal, Principal principal){
        String name = principal.getName();
        long userId = userDao.findIdByUsername(name);
        meal.setUserId(Math.toIntExact(userId));
        return mealDao.getMealId(meal);
    }

    @GetMapping("/meal/{mealId}")
    public Meal getMealById(@Valid @PathVariable long mealId){
        return mealDao.getMealById(mealId);
    }

    @GetMapping("/allMeals")
    public List<Meal> getAllMeals(Principal principal) {
        long userId = userDao.findIdByUsername(principal.getName());

        return mealDao.getAllMeals(userId);
    }

    @RequestMapping(path = "/addMealItems/{mealId}", method = RequestMethod.POST)
    public void addMealItems(@Valid @RequestBody List<FoodItem> items, @PathVariable int mealId){
        mealDao.addMealItem(items, mealId);
    }



    @GetMapping("/quickMeals")
    public List<Meal> getQuickMeals(Principal principal){
        long userId = userDao.findIdByUsername(principal.getName());
        return mealDao.getQuickMeals(userId);
    }

    @RequestMapping(path="/addStars/{mealDate}", method = RequestMethod.PUT)
    public int calculateStars(@PathVariable String mealDate, Principal principal){
        LocalDate mDate = LocalDate.parse(mealDate);
        String name = principal.getName();
        long userId = userDao.findIdByUsername(name);

       return mealDao.trackMealsForStars(mDate, (int)userId);
    }


    @GetMapping(path="/getStars")
    public int getStars(@Valid Principal principal){
        String name = principal.getName();
        long userId = userDao.findIdByUsername(name);
        return userProfileDao.getStarAmount((int) userId);
    }

    @PutMapping("/deleteMeal/{mealName}/{mealDate}")
    public void deleteFood(@RequestBody int[] foodId, @PathVariable String mealName, @PathVariable String mealDate,  Principal principal){

        String name = principal.getName();
        long userId = userDao.findIdByUsername(name);

        Meal meal = new Meal();
        meal.setUserId(Math.toIntExact(userId));
        meal.setMealName(mealName);
        meal.setMealDate(LocalDate.parse(mealDate));
        long mealId = mealDao.getMealId(meal);

      foodItemDao.deleteFood(mealName, meal.getMealDate(), foodId, mealId);
    }
    //Food methods
    @RequestMapping(path = "/foods", method = RequestMethod.GET)
    public List<FoodItem> searchFoods(@RequestParam String food) {
        return foodItemDao.searchFood(food);
    }

    @RequestMapping(path = "/food/{fdcId}", method = RequestMethod.GET)
    public FoodItem foodNutrition(@PathVariable int fdcId){
        return foodItemDao.getFoodNutrition(fdcId);
    }

    @GetMapping("/quickFoods")
    public List<FoodItem> getQuickFoods(@RequestParam  Principal principal) {
        long userId = userDao.findIdByUsername(principal.getName());

        return foodItemDao.getQuickFoods(userId);
    }

    @RequestMapping(path = "/getMealFoods", method = RequestMethod.POST)
    public List<FoodItem> getFoodsForMeal(@RequestBody Meal currentMeal, Principal principal){
        String name = principal.getName();
        int userId = userDao.findIdByUsername(name);
        currentMeal.setUserId(userId);

        return foodItemDao.getAllFoodFromMeal(currentMeal);
    }

    @GetMapping("/progressCalories/{timeFrame}")
    public List<Double> getProgressCalories (@RequestParam  Principal principal, int timeFrame) {
        long userId = userDao.findIdByUsername(principal.getName());

        return progressDao.getCaloriesByIDAndDate(userId,timeFrame);
    }
    @GetMapping("/progressFat/{timeFrame}")
    public List<Double> getProgressFat (@RequestParam  Principal principal, int timeFrame) {
        long userId = userDao.findIdByUsername(principal.getName());

        return progressDao.getFatByIDAndDate(userId,timeFrame);
    }
    @GetMapping("/progressFiber/{timeFrame}")
    public List<Double> getProgressFiber(@RequestParam  Principal principal, int timeFrame) {
        long userId = userDao.findIdByUsername(principal.getName());

        return progressDao.getFiberByIDAndDate(userId,timeFrame);
    }
    @GetMapping("/progressSugar/{timeFrame}")
    public List<Double> getProgressSugar (@RequestParam  Principal principal, int timeFrame) {
        long userId = userDao.findIdByUsername(principal.getName());

        return progressDao.getSugarByIDAndDate(userId,timeFrame);
    }
    @GetMapping("/progressCarbs/{timeFrame}")
    public List<Double> getProgressCarbs (@RequestParam  Principal principal, int timeFrame) {
        long userId = userDao.findIdByUsername(principal.getName());

        return progressDao.getCarbsByIDAndDate(userId,timeFrame);
    }@GetMapping("/progressProtein/{timeFrame}")
    public List<Double> getProgressProtein (@RequestParam  Principal principal, int timeFrame) {
        long userId = userDao.findIdByUsername(principal.getName());

        return progressDao.getProteinByIDAndDate(userId,timeFrame);
    }@GetMapping("/progressCholesterol/{timeFrame}")
    public List<Double> getProgressCholesterol (@RequestParam  Principal principal, int timeFrame) {
        long userId = userDao.findIdByUsername(principal.getName());

        return progressDao.getCholesterolByIDAndDate(userId,timeFrame);
    }@GetMapping("/progressSodium/{timeFrame}")
    public List<Double> getProgressSodium (@RequestParam  Principal principal, int timeFrame) {
        long userId = userDao.findIdByUsername(principal.getName());

        return progressDao.getSodiumByIDAndDate(userId, timeFrame);


    }

    @RequestMapping(path = "/getCalorieByDateRange", method=RequestMethod.POST)
    public List<CaloriesPerDay> getCaloriesPerDay(@RequestBody CaloriesPerDay calPerDay,  Principal principal){
        long userId = userDao.findIdByUsername(principal.getName());
        calPerDay.setUserId(userId);
        return progressDao.getCaloriesByDay(calPerDay);
    }

    @RequestMapping(path="/getMealSummary", method=RequestMethod.GET)
    public List<MealSummary> getMealSummary(Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        return progressDao.getMealSummary(userId);
    }
}