package com.zn.xzrpc;

import com.zn.xzrpc.config.RegistryConfig;
import com.zn.xzrpc.config.RpcConfig;
import com.zn.xzrpc.constant.RpcConstant;
import com.zn.xzrpc.register.Registry;
import com.zn.xzrpc.register.RegistryFactory;
import com.zn.xzrpc.uitls.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * RPC框架应用
 */
@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;


    /**
     * 初始化配置
     */
    public static void init() {
        RpcConfig config;
        try {
            config = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            //配置加载失败，使用默认配置
            config = new RpcConfig();
        }
        init(config);
    }


    /**
     * 框架初始化，
     * @param config
     */
    public static void init(RpcConfig config) {
        rpcConfig = config;
        log.info("rpc init: config = {}", config);

        // 注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init, config = {}", registryConfig);
    }

    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized(RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }

}
