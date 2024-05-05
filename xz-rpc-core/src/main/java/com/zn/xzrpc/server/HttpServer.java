package com.zn.xzrpc.server;

/**
 * Http服务器接口
 */
public interface HttpServer {

    /**
     * 启动服务器
     * @param port 端口
     */
    void doStart(int port);
}
