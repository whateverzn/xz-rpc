package com.zn.xzrpc.config;

import com.zn.xzrpc.constant.SerializerKeys;
import com.zn.xzrpc.register.Registry;
import lombok.Data;

/**
 * RPC配置类
 */
@Data
public class RpcConfig {
    /**
     * 名称
     */
    private String name = "xz-rpc";

    /**
     * 版本
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 9090;

    /**
     * mock模式
     */
    private Boolean mock = false;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

}
