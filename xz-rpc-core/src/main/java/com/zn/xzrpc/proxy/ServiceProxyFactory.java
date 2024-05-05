package com.zn.xzrpc.proxy;

import com.zn.xzrpc.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * 服务代理工厂
 */
public class ServiceProxyFactory {

    /**
     * 根据服务类获取代理对象
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> T getProxy(Class<T> clazz) {
        if (RpcApplication.getRpcConfig().getMock()) {
            return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                    new Class[]{clazz},
                    new MockServiceProxy());
        }
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                new ServiceProxy());
    }

}
