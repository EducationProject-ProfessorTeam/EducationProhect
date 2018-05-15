package com.mapper;

import com.domain.User;

import java.util.List;

public interface UserMapper {
    void saveUser(User user);
    User getUserByUserId(String userId);
    String getUserIdByUsernameAndPassword(User user);
    String getUserIdByUsername(String username);
    List<User> getUserList();
}
