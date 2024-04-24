package com.zn.example.provider;

import com.zn.xzrpc.server.VertxHttpServer;

/**
 * 简易服务提供者
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        VertxHttpServer server = new VertxHttpServer();
        server.doStart(8080);
    }

}
