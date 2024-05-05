package com.zn.example.consumer;

import com.zn.example.common.model.User;
import com.zn.example.common.service.UserService;
import com.zn.xzrpc.RpcApplication;
import com.zn.xzrpc.config.RpcConfig;
import com.zn.xzrpc.proxy.ServiceProxyFactory;

/**
 * 消费者实例
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        System.out.println(rpcConfig);

        // TODO 通过服务拉取获得服务
//        UserService userService = new UserServiceProxy();
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
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
