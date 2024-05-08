package com.zn.example.provider;

import com.zn.example.common.service.UserService;
import com.zn.xzrpc.RpcApplication;
import com.zn.xzrpc.config.RegistryConfig;
import com.zn.xzrpc.config.RpcConfig;
import com.zn.xzrpc.model.ServiceMetaInfo;
import com.zn.xzrpc.register.LocalRegistry;
import com.zn.xzrpc.register.Registry;
import com.zn.xzrpc.register.RegistryFactory;
import com.zn.xzrpc.server.HttpServer;
import com.zn.xzrpc.server.VertxHttpServer;

/**
 * 简易服务提供者
 */
public class EasyProviderExample {


    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(9090);
        serviceMetaInfo.setServiceAddress(rpcConfig.getServerHost() + ":" + rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }

//    public static void main(String[] args) {
//        //注册服务
//        LocalRegister.register(UserService.class.getName(), UserServiceImpl.class);
//        VertxHttpServer server = new VertxHttpServer();
////        server.doStart(8080);
//        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
//        server.doStart(rpcConfig.getServerPort());
//    }

}
