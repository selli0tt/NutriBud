package com.techelevator.dao;

import com.techelevator.model.User;
import org.springframework.data.jdbc.repository.query.Query;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    User getUserById(Long userId);

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password, String role);

    void changePassword(String user, String newPassword);


}
