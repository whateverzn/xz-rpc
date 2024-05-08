package com.zn.xzrpc.register;

import com.zn.xzrpc.spi.RpcSpiLoader;

/**
 * 注册中心工厂（用于获取注册中心对象）
 */
public class RegistryFactory {

    static {
        RpcSpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegister();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static Registry getInstance(String key) {
        return RpcSpiLoader.getInstance(Registry.class, key);
    }

}