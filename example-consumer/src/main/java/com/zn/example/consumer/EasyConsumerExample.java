package com.zn.example.consumer;

import com.zn.example.common.model.User;
import com.zn.example.common.service.UserService;

/**
 * 消费者实例
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        // TODO 通过服务拉取获得服务
        UserService userService = null;
        User user = new User();
        user.setName("John");
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(user);
        } else {
            System.out.println("No user...");
        }
    }

}
