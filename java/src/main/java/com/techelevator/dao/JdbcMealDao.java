package com.techelevator.dao;

import com.techelevator.model.FoodItem;
import com.techelevator.model.Meal;
import com.techelevator.model.UserProfile;
import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@PreAuthorize("isAuthenticated()")
@Component

public class JdbcMealDao implements MealDao{

    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserProfileDao userProfileDao;

    public JdbcMealDao(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate;}



    @Override
    public int create( Meal meal) {

        String sql = "INSERT into meal ( user_id, meal_name, meal_date, saved) values (?,?,?,?) RETURNING meal_id";
        Integer mealId = 0;

        try{
            mealId = jdbcTemplate.queryForObject(sql, Integer.class, meal.getUserId(), meal.getMealName(), meal.getMealDate(), meal.isSaved());

        } catch (DataAccessException e){
        }

        return mealId;


    }
    @Override
    public Meal getMealById(long mealId) {
        Meal meal = new Meal();
        String sql = "SELECT meal_id, user_id, meal_name, meal_date, saved " +
                "FROM meal WHERE meal_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, mealId);

        while(results.next()){
            meal = mapRowToMeal(results);

        }
        return meal;
    }

    @Override
    public Meal getLastMeal(int userId){
        String sql = "SELECT meal_id FROM meal WHERE user_id = ? ORDER BY meal_date DESC, meal_name='Snacks', meal_name='Breakfast', meal_name='Lunch', meal_name='Dinner' LIMIT 1;";
        long mealId = jdbcTemplate.queryForObject(sql, Long.class, userId);
        return this.getMealById(mealId);
    }

    @Override
    public void addMealItem(List<FoodItem> items, int mealId){
        String sql = "INSERT INTO meal_item (meal_id, food_id) VALUES (?,?)";
        for(FoodItem food : items){
            try {
                jdbcTemplate.update(sql, mealId, food.getFoodId());
            } catch(Exception e){

            }
        }
    }

    @Override
    public List<Meal> getAllMeals(long userId){
        List<Meal> meals = new ArrayList<>();
        String sql = "SELECT meal_id, user_id, meal_name, meal_date, saved " +
                " FROM meal WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while(results.next()){
            Meal meal = mapRowToMeal(results);
            meals.add(meal);

        }
        return meals;
    }

    @Override
    public void deleteMeal(long mealId){
        String sql = "DELETE FROM meal_item WHERE meal_id = ?; " +
                     "DELETE FROM meal WHERE meal_id = ? ";
        jdbcTemplate.update(sql,mealId);
    }

    @Override
    public List<Meal> getQuickMeals(long userId) {
        List<Meal> meals = new ArrayList<>();
        String sql = "SELECT meal_name, meal_date FROM meal " +
                "WHERE user_id=? AND meal_date > CURRENT_DATE - INTERVAL '7 days' AND meal_date< CURRENT_DATE";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,userId);

        while(results.next()){
            Meal meal = mapRowToMeal(results);
            meals.add(meal);
        }
        return meals;
    }


    @Override
    public int getMealId(Meal meal){
        int mealId =0;
        String sql = "SELECT meal_id from meal WHERE user_id = ? AND meal_name = ? AND meal_date = ?";
        try {
            mealId = jdbcTemplate.queryForObject(sql, Integer.class, meal.getUserId(), meal.getMealName(), meal.getMealDate());
        }catch (Exception e) {

        }

        return mealId;
    }

    public String getDateByMeal(long mealId){
        String sql = "Select meal_date " +
                "FROM meal " +
                "WHERE meal_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, mealId);

    }

    public List<Meal> getMealsByFoodIdAndMealId(long foodId, long mealId){
        List<Meal> meals = new ArrayList<>();
        String sql = "SELECT meal.user_id,meal.meal_name,meal.meal_date,meal.saved " +
                "FROM meal " +
                "JOIN meal_item ON meal.meal_id = meal_item.meal_id " +
                "JOIN food_item ON meal_item.food_id = food_item.food_id " +
                "WHERE food_item.food_id = ? and meal_item.meal_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, foodId, mealId);

        while(results.next()){
            Meal meal = mapRowToMeal(results);
            meals.add(meal);

        }
        return meals;

    }


    public int trackMealsForStars(LocalDate mealDate, int userId) {
        //Gets yesterdays date
        LocalDate yesterdayDate = mealDate.minusDays(1);
        //Determining how many meals were eaten by user on a particular day

        String sql = "SELECT COUNT(meal_id) " +
                "FROM meal " +

                "WHERE meal.meal_date = ? AND meal.user_id = ? ";
        ;

        int mealsToday = jdbcTemplate.queryForObject(sql, Integer.class, mealDate, userId);
        // # of meals yesterday
        String sqlYesterday = "SELECT COUNT(meal_id) " +
                "FROM meal " +

                "WHERE meal.meal_date = ? AND meal.user_id = ? ";
        ;
        int yesterdayCount = jdbcTemplate.queryForObject(sqlYesterday, Integer.class, yesterdayDate, userId);

        //Current stars in profile
        int countStars = 0;
        UserProfile mealsPerDay = userProfileDao.getProfileById(userId);
        int currentStarBalance = userProfileDao.getStarAmount(userId);
        countStars = currentStarBalance;

        //if not enough meals yesterday it zeros i
        System.out.println("Yesterday Count is: " + yesterdayCount + " Meal Goal is: " + mealsPerDay.getMealsPerDay() + " current star balance: " + currentStarBalance);
        if (yesterdayCount < mealsPerDay.getMealsPerDay()) {
            countStars = 0;
            String sqlUpdate = "UPDATE user_profile " +
                    "SET star_count = ? " +
                    "WHERE user_id = ?";
            jdbcTemplate.update(sqlUpdate, countStars, userId);
        }

        if (mealsPerDay.getMealsPerDay() == mealsToday) {
            countStars += 1;
            String sqlUpdate = "UPDATE user_profile " +
                    "SET star_count = ? " +
                    "WHERE user_id = ?";
            jdbcTemplate.update(sqlUpdate, countStars, userId);
            System.out.println(countStars + " stars added today");


        }
        return countStars;
    }

//        System.out.println(userId + "check user id"); //
//        //Determining how many meals were eaten by user on a particular day
//=======
//>>>>>>> main
////        String sql = "SELECT COUNT(meal_id) " +
////                "FROM meal " +
////
////                "WHERE meal.meal_date = ? AND meal.user_id = ? ";;
////
////        int  mealsToday = jdbcTemplate.queryForObject(sql, Integer.class, mealDate, userId);
////        // # of meals yesterday
////        String sqlYesterday ="SELECT COUNT(meal_id) " +
////                "FROM meal " +
////
////                "WHERE meal.meal_date = ?-1 AND meal.user_id = ? ";;
////        int  yesterdayCount = jdbcTemplate.queryForObject(sqlYesterday, Integer.class, mealDate, userId);
////
////        //Current stars in profile
////        int countStars = 0;
////        UserProfile mealsPerDay = userProfileDao.getProfileById(userId);
////        int currentStarBalance = userProfileDao.getStarAmount(userId);
////
////        //if not enough meals yesterday it zeros i
////
////        if (yesterdayCount < mealsPerDay.getMealsPerDay()) {
////            countStars = 0;
////            String sqlUpdate = "UPDATE user_profile " +
////                    "SET star_count = ? " +
////                    "WHERE user_id = ?";
////            jdbcTemplate.update(sqlUpdate, countStars, userId);
////        }
////
////        if(mealsPerDay.getMealsPerDay() == mealsToday){
////            countStars = currentStarBalance + 1;
////            String sqlUpdate = "UPDATE user_profile " +
////                    "SET star_count = ? " +
////                    "WHERE user_id = ?";
////            jdbcTemplate.update(sqlUpdate, countStars, userId);
////            System.out.println(countStars+" stars added today");
////
////
////        }
////        return countStars;
//
//
//
//        //Determining how many meals were eaten by user on a particular day
//        String sql = "SELECT COUNT(meal_id) " +
//                "FROM meal " +
//
//                "WHERE meal.meal_date = ? AND meal.user_id = ? ";;
//
//        int  mealsToday = jdbcTemplate.queryForObject(sql, Integer.class, mealDate, userId);
//
//        String sqlYesterday ="SELECT COUNT(meal_id) " +
//                "FROM meal " +
//
//                "WHERE meal.meal_date = ?-1 AND meal.user_id = ? ";;
//        int  yesterdayCount = jdbcTemplate.queryForObject(sqlYesterday, Integer.class, mealDate, userId);
//
//
//        //Current stars in profile
//        UserProfile mealsPerDay = userProfileDao.getProfileById(userId);
//        int currentStarBalance = userProfileDao.getStarAmount(userId);
//
//       if(mealsPerDay.getMealsPerDay() <= mealsToday){
//
//           currentStarBalance = yesterdayCount +1;
//
//            String sqlUpdate = "UPDATE user_profile " +
//                    "SET star_count = ? " +
//                    "WHERE user_id = ?";
//            jdbcTemplate.update(sqlUpdate, currentStarBalance, userId);
//            return currentStarBalance;
//
//        }
//
//       if(mealsPerDay.getMealsPerDay() > mealsToday){
//
//            currentStarBalance = 0;
//            String sqlUpdate = "UPDATE user_profile " +
//                    "SET star_count = ? " +
//                    "WHERE user_id = ?";
//            jdbcTemplate.update(sqlUpdate, currentStarBalance, userId);
//            return currentStarBalance;
//        }
//
//        return currentStarBalance;
//    }

    private Meal mapRowToMeal(SqlRowSet results){
        Meal meal = new Meal();
        meal.setMealId(results.getLong("meal_id"));
        meal.setUserId(results.getInt("user_id"));
        meal.setMealName(results.getString("meal_name"));
        meal.setMealDate(LocalDate.parse(results.getString("meal_date")));


        meal.setSaved(results.getBoolean("saved"));

        return meal;
    }
}
