package com.techelevator.dao;

import com.techelevator.model.CaloriesPerDay;
import com.techelevator.model.FoodItem;
import com.techelevator.model.MealSummary;
import org.apache.tomcat.jni.Local;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Component

public class JdbcProgressDao implements ProgressDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcProgressDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Double> getCaloriesByIDAndDate(Long userId, int days) {

        List<Double> caloriesPerDay = new ArrayList<>();
        String sql = "SELECT calories FROM food_item JOIN meal_item ON meal_item.food_id = food_item.food_id JOIN meal ON meal.meal_id = meal_item.meal_id " +
                "WHERE user_id=? AND meal_date > CURRENT_DATE - INTERVAL '?' AND meal_date <= CURRENT_DATE";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, days);

        return caloriesPerDay;
    }

    @Override
    public List<CaloriesPerDay> getCaloriesByDay(CaloriesPerDay calPerDay){
        System.out.println(calPerDay);
        System.out.println(calPerDay.getUserId());
        System.out.println(calPerDay.getEndDate());
        List<CaloriesPerDay> caloriesPerDayList = new ArrayList<>();
         String sql = "Select sum(calories) AS calories, meal_date from" +
                "(Select user_id, food_item.food_id, calories, meal_date from food_item inner join meal_item on food_item.food_id = meal_item.food_id inner join meal on meal_item.meal_id = meal.meal_id) as foodGroup where user_id = ? AND meal_date between ? AND ?" +
                " group by meal_date;";
         SqlRowSet results = jdbcTemplate.queryForRowSet(sql, calPerDay.getUserId(), calPerDay.getStartDate(), calPerDay.getEndDate());

         while (results.next()){
            CaloriesPerDay current = new CaloriesPerDay();
            current.setSum(results.getInt("calories"));
            current.setCalorieDate(LocalDate.parse(results.getString("meal_date")));
            System.out.println(current.getSum() +""+ current.getCalorieDate());
            caloriesPerDayList.add(current);

         }
         return caloriesPerDayList;

    }


    @Override
    public List<Double> getCarbsByIDAndDate(Long userId, int days) {
        List<Double> carbsPerDay = new ArrayList<>();
        String sql = "SELECT carbohydrates FROM food_item JOIN meal_item ON meal_item.food_id = food_item.food_id JOIN meal ON meal.meal_id = meal_item.meal_id " +
                "WHERE user_id=? AND meal_date > CURRENT_DATE - INTERVAL '?' AND meal_date <= CURRENT_DATE";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, days);

        return carbsPerDay;
    }
    @Override
    public List<Double> getFatByIDAndDate(Long userId, int days) {

        List<Double> getFatByIDAndDate = new ArrayList<>();
        String sql = "SELECT fat FROM food_item JOIN meal_item ON meal_item.food_id = food_item.food_id JOIN meal ON meal.meal_id = meal_item.meal_id " +
                "WHERE user_id=? AND meal_date > CURRENT_DATE - INTERVAL '?' AND meal_date <= CURRENT_DATE";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, days);

        return getFatByIDAndDate;
    }
    @Override
    public List<Double> getProteinByIDAndDate(Long userId, int days) {
        List<Double> proteinPerDay = new ArrayList<>();
        String sql = "SELECT protein FROM food_item JOIN meal_item ON meal_item.food_id = food_item.food_id JOIN meal ON meal.meal_id = meal_item.meal_id " +
                "WHERE user_id=? AND meal_date > CURRENT_DATE - INTERVAL '?' AND meal_date <= CURRENT_DATE";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, days);

        return proteinPerDay;
    }

    @Override
    public List<Double> getCholesterolByIDAndDate(Long userId, int days) {
        List<Double> cholesterolPerDay = new ArrayList<>();
        String sql = "SELECT cholesterol FROM food_item JOIN meal_item ON meal_item.food_id = food_item.food_id JOIN meal ON meal.meal_id = meal_item.meal_id " +
                "WHERE user_id=? AND meal_date > CURRENT_DATE - INTERVAL '?' AND meal_date <= CURRENT_DATE";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, days);

        return cholesterolPerDay;
    }

    @Override
    public List<Double> getSodiumByIDAndDate(Long userId, int days) {
        List<Double> sodiumPerDay = new ArrayList<>();
        String sql = "SELECT sodium FROM food_item JOIN meal_item ON meal_item.food_id = food_item.food_id JOIN meal ON meal.meal_id = meal_item.meal_id " +
                "WHERE user_id=? AND meal_date > CURRENT_DATE - INTERVAL '?' AND meal_date <= CURRENT_DATE";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, days);

        return sodiumPerDay;
    }

    @Override
    public List<Double> getFiberByIDAndDate(Long userId, int days) {
        List<Double> fiberPerDay = new ArrayList<>();
        String sql = "SELECT fiber FROM food_item JOIN meal_item ON meal_item.food_id = food_item.food_id JOIN meal ON meal.meal_id = meal_item.meal_id " +
                "WHERE user_id=? AND meal_date > CURRENT_DATE - INTERVAL '?' AND meal_date <= CURRENT_DATE";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, days);

        return fiberPerDay;
    }

    @Override
    public List<Double> getSugarByIDAndDate(Long userId, int days) {
        List<Double> sugarPerDay = new ArrayList<>();
        String sql = "SELECT sugar FROM food_item JOIN meal_item ON meal_item.food_id = food_item.food_id JOIN meal ON meal.meal_id = meal_item.meal_id " +
                "WHERE user_id=? AND meal_date > CURRENT_DATE - INTERVAL '?' AND meal_date <= CURRENT_DATE";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, days);

        return sugarPerDay;
    }

    @Override
    public List<MealSummary> getMealSummary(int userId){
        List<MealSummary> returnList = new ArrayList<>();
        String sql = "Select meal_date, sum(calories) AS calories, sum(carbohydrates) AS carbohydrates, sum(protein) AS protein, sum(fat) as fat from " +
                "(Select user_id, food_item.food_id, calories, meal_date, carbohydrates, protein, fat from food_item " +
                "inner join meal_item on food_item.food_id = meal_item.food_id inner join meal on meal_item.meal_id = meal.meal_id) " +
                "as foodGroup where user_id = ? group by meal_date ORDER BY meal_date DESC LIMIT 3 ";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while(results.next()){
            MealSummary current = new MealSummary();
            current.setMealDate(LocalDate.parse(results.getString("meal_date")));
            current.setCalories(results.getInt("calories"));
            current.setCarbohydrates(results.getDouble("carbohydrates"));
            current.setProtein(results.getDouble("protein"));
            current.setFat(results.getDouble("fat"));
            returnList.add(current);
        }
        return returnList;
    }
}
