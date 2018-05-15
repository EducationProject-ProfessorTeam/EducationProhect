package com.service;

import com.domain.User;

public interface UserService {
    void saveUser(User user);
    User getUserByUserId(String userId);
    String getUserIdByUsernameAndPassword(User user);
    String getUserIdByUsername(String username);
}
