package com.zn.example.provider;

import com.zn.example.common.model.User;
import com.zn.example.common.service.UserService;

/**
 * 用户服务实现类
 */
public class UserServiceImpl implements UserService {


    @Override
    public User getUser(User user) {
        System.out.println("user: name + " + user.getName());
        return user;
    }
}
