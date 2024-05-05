package com.zn.example.common.service;

import com.zn.example.common.model.User;

/**
 * 用户服务例子
 */

public interface UserService {

    User getUser(User user);

    default int getNumber() {
        return 1;
    }

}
