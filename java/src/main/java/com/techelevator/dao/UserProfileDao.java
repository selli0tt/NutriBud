package com.techelevator.dao;

import com.techelevator.model.UserProfile;
import org.springframework.data.jdbc.repository.query.Query;

public interface UserProfileDao {

    void create(UserProfile user);

    UserProfile getProfileById(int id);

    void editUserProfile(UserProfile features);

    int getStarAmount(int userId);

    String gerUserProfileImage(int userId);

}
