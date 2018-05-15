package com.service.impl;

import com.domain.User;
import com.mapper.UserMapper;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
//    自动注入
    @Autowired
    private UserMapper userMapper;

    @Override
    public void saveUser(User user) {
        userMapper.saveUser(user);
    }

    @Override
    public User getUserByUserId(String userId) {

        User user = userMapper.getUserByUserId(userId);
        return user;
    }

    @Override
    public String getUserIdByUsernameAndPassword(User user) {
        String userId = userMapper.getUserIdByUsernameAndPassword(user);
        return userId;
    }

    @Override
    public String getUserIdByUsername(String username) {
        String userId = userMapper.getUserIdByUsername(username);
        return userId;
    }
}
