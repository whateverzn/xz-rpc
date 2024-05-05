package com.zn.example.provider;

import com.zn.example.common.service.UserService;
import com.zn.xzrpc.RpcApplication;
import com.zn.xzrpc.config.RpcConfig;
import com.zn.xzrpc.register.LocalRegister;
import com.zn.xzrpc.server.VertxHttpServer;

/**
 * 简易服务提供者
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        //注册服务
        LocalRegister.register(UserService.class.getName(), UserServiceImpl.class);
        VertxHttpServer server = new VertxHttpServer();
//        server.doStart(8080);
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        server.doStart(rpcConfig.getServerPort());
    }

}
