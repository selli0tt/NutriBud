package com.techelevator.dao;

import com.techelevator.model.UserProfile;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@PreAuthorize("isAuthenticated()")
@Component

public class JdbcUserProfileDao implements UserProfileDao {

    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;

    public JdbcUserProfileDao(DataSource dataSource){jdbcTemplate = new JdbcTemplate((dataSource));}

    public void editUserProfile(UserProfile features){
        System.out.println(features);
        String sql = "UPDATE user_profile " +
                "SET first_name = ?, last_name = ?, height = ?, desired_weight = ?, current_weight = ?, birthday = ?, calorie_goal = ?, display_name = ?, meals_per_day = ?, file_data = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql,features.getFirstName(), features.getLastName(), features.getHeight(), features.getDesiredWeight(), features.getCurrentWeight(), features.getBirthday(), features.getCalorieGoal(), features.getDisplayName(), features.getMealsPerDay(), features.getFileData(), features.getUserId());
    }

    @Override
    public void create(UserProfile user) {

        String insertProfile = "INSERT into user_profile (user_id, first_name, last_name, age, birthday, height, current_weight, desired_weight, display_name, file_data, calorie_goal, meals_per_day, star_count) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        jdbcTemplate.update(insertProfile, user.getUserId(), user.getFirstName(),user.getLastName(),user.getAge(),user.getBirthday(),user.getHeight(), user.getCurrentWeight(),user.getDesiredWeight(), user.getDisplayName(), user.getFileData(), user.getCalorieGoal(), user.getMealsPerDay(), user.getStarCount());
    }

    @Override
    public UserProfile getProfileById(int id) {
        UserProfile profile = new UserProfile();
        String sql = "SELECT user_id, first_name, last_name, age, birthday, height, current_weight, desired_weight, display_name, calorie_goal, meals_per_day, file_data, star_count " +
                "FROM user_profile WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

        while(results.next()){
            profile = mapRowToUserProfile(results);

        }
        return profile;
    }

    public int getStarAmount(int userId){
        String sql = "SELECT star_count " +
                "FROM user_profile " +
                "where user_id = ?";

        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }

    public String gerUserProfileImage(int userId){
        String sql = "SELECT file_data " +
                "from user_profile " +
                "where user_id = ?";

        return jdbcTemplate.queryForObject(sql, String.class, userId);
    }



    private UserProfile mapRowToUserProfile(SqlRowSet results){
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(results.getLong("user_id"));
        userProfile.setFirstName(results.getString("first_name"));
        userProfile.setLastName(results.getString("last_name"));
        userProfile.setAge(results.getInt("age"));
        try {
            userProfile.setBirthday(LocalDate.parse(results.getString("birthday")));
        } catch (NullPointerException e){
            System.out.println("User Birthday is null. All is okay.");
        }
        userProfile.setHeight(results.getDouble("height"));
        userProfile.setCurrentWeight(results.getDouble("current_weight"));
        userProfile.setDesiredWeight(results.getDouble("desired_weight"));
        userProfile.setCalorieGoal(results.getInt("calorie_goal"));
        userProfile.setDisplayName(results.getString("display_name"));
        userProfile.setMealsPerDay(results.getInt("meals_per_day"));
        userProfile.setFileData((results.getString("file_data")));
        userProfile.setStarCount((results.getInt("star_count")));
        return userProfile;
    }

}
