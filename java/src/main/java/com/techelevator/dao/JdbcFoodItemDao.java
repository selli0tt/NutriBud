package com.techelevator.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.model.FoodItem;
import com.techelevator.model.Meal;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class JdbcFoodItemDao implements FoodItemDao {

    private ObjectMapper objectMapper = new ObjectMapper();
    private JdbcTemplate jdbcTemplate;
    private RestTemplate restTemplate = new RestTemplate();
    private String apiKey = "api_key=SJ4mmAOp5D6xwPp9SlADTZMLfSZj3hencSlEhirR";
    private String nutrients = "208,205,204,203,601,307,291,269";

    public JdbcFoodItemDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate((dataSource));
    }



    String API_BASE_URL = "https://api.nal.usda.gov/fdc/v1/";

    @Override
    public List<FoodItem> searchFood(String searchTerm) {
        List<FoodItem> returnItems = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        String returnObj = restTemplate.exchange(API_BASE_URL + "foods/search?query=" + searchTerm + "&" + apiKey, HttpMethod.GET, entity, String.class).getBody();
        try {
            JsonNode returnNode = objectMapper.readTree(returnObj);
            for (int i = 0; i < 10; i++) {
                String foodName = returnNode.path("foods").path(i).path("description").asText();
                String fdcId = returnNode.path("foods").path(i).path("fdcId").asText();
                long foodId = Long.parseLong(fdcId);
                FoodItem current = new FoodItem(foodId, foodName);
                returnItems.add(current);
            }
        } catch (Exception e) {
        }
        return returnItems;

    }


    @Override
    public void deleteFood( String mealName,  LocalDate mealDate, int[] foodId, long mealId) {
        String sql = "DELETE FROM meal_item WHERE food_id = ? AND meal_id = ?  ";

        for (int i = 0; i < foodId.length; i++) {
            jdbcTemplate.update(sql, foodId[i], mealId);
        }
    }

    @Override
    public FoodItem getFoodNutrition(int fdcId) {
        FoodItem returnItem = new FoodItem();
        returnItem.setFoodId(fdcId);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        String returnObj = restTemplate.exchange(API_BASE_URL + "food/" + fdcId + "?nutrients=" + nutrients + "&" + apiKey, HttpMethod.GET, entity, String.class).getBody();
        try {
            JsonNode returnNode = objectMapper.readTree(returnObj);
            returnItem.setFoodType(returnNode.path("description").asText());
            for (int i = 0; i < returnNode.size(); i++) {
                int currentNum = returnNode.path("foodNutrients").path(i).path("nutrient").path("number").asInt();
                double currentAmount = returnNode.path("foodNutrients").path(i).path("amount").asDouble();
                setFoodItemNutrients(returnItem, currentNum, currentAmount);
            }
            String sql = "INSERT into food_item (food_id, food_type, calories, carbohydrates, fat, protein, cholesterol, sodium, fiber, sugar) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?)";
            jdbcTemplate.update(sql, returnItem.getFoodId(), returnItem.getFoodType(), returnItem.getCalories(), returnItem.getCarbohydrates(),
                    returnItem.getFat(), returnItem.getProtein(), returnItem.getCholesterol(), returnItem.getSodium(), returnItem.getFiber(), returnItem.getSugar());
        } catch (Exception e) {
        }

        return returnItem;
    }

    @Override
    public List<FoodItem> getQuickFoods(long userId) {
// will come back and finish this once create meal has food item and check progress function
        List<FoodItem> foods = new ArrayList<>();
        String sql = "SELECT food_type FROM food_item " +
                "JOIN meal_item ON meal_item.food_id = food_item.food_id JOIN meal ON meal.meal_id = meal_item.meal_id " +
                "WHERE user_id=? AND meal_date > CURRENT_DATE - INTERVAL '7 days' AND meal_date< CURRENT_DATE";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while (results.next()) {
            FoodItem food = mapRowToFoodItem(results);
            foods.add(food);
        }
        return foods;
    }

    @Override
    public List<FoodItem> getAllFoodFromMeal(Meal meal){
        List<FoodItem> returnFoods = new ArrayList<>();

        String sql = "SELECT food_id, food_type, calories, carbohydrates, fat, protein, cholesterol, sodium, fiber, sugar FROM food_item WHERE food_id IN (SELECT food_id FROM meal JOIN meal_item ON" +
                "                (meal_item.meal_id = meal.meal_id) WHERE user_id = ? AND meal_name = ? AND meal_date = ?)";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, meal.getUserId(), meal.getMealName(), meal.getMealDate());

        while(results.next()){
            FoodItem food = mapRowToFoodItem(results);
            returnFoods.add(food);
        }

        return returnFoods;
    }


    public void setFoodItemNutrients(FoodItem foodItem, int nutrientNum, double amount) {
        switch (nutrientNum) {
            case 208:
                foodItem.setCalories(amount);
                break;
            case 205:
                foodItem.setCarbohydrates(amount);
                break;
            case 204:
                foodItem.setFat(amount);
                break;
            case 203:
                foodItem.setProtein(amount);
                break;
            case 601:
                foodItem.setCholesterol(amount);
                break;
            case 307:
                foodItem.setSodium(amount);
                break;
            case 291:
                foodItem.setFiber(amount);
                break;
            case 269:
                foodItem.setSugar(amount);
                break;
        }
    }

    public FoodItem mapRowToFoodItem(SqlRowSet results) {
        FoodItem food = new FoodItem();
        food.setFoodId(results.getLong("food_id"));
        food.setFoodType(results.getString("food_type"));
        food.setCalories(results.getDouble("calories"));
        food.setCarbohydrates(results.getDouble("carbohydrates"));
        food.setFat(results.getDouble("fat"));
        food.setProtein(results.getDouble("protein"));
        food.setCholesterol(results.getDouble("cholesterol"));
        food.setSodium(results.getDouble("sodium"));
        food.setFiber(results.getDouble("fiber"));
        food.setSugar(results.getDouble("sugar"));


        return food;
    }
}

