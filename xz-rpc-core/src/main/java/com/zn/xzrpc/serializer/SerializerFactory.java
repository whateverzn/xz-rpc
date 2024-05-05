package com.zn.xzrpc.serializer;

import com.zn.xzrpc.spi.RpcSpiLoader;

/**
 * 序列化工厂
 */
public class SerializerFactory {

    static {
        RpcSpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {
        return RpcSpiLoader.getInstance(Serializer.class, key);
    }

}