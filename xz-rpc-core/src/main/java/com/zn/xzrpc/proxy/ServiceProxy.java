package com.zn.xzrpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zn.xzrpc.RpcApplication;
import com.zn.xzrpc.model.RpcRequest;
import com.zn.xzrpc.model.RpcResponse;
import com.zn.xzrpc.serializer.JdkSerializer;
import com.zn.xzrpc.serializer.Serializer;
import com.zn.xzrpc.serializer.SerializerFactory;
import com.zn.xzrpc.spi.RpcSpiLoader;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 服务代理类 (JDK动态代理）
 */
public class ServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        Serializer serializer = new JdkSerializer();
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        System.out.println(proxy.getClass());
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .parameterTypes(method.getParameterTypes())
                .build();

        try {
            byte[] bytes = serializer.serialize(rpcRequest);
            byte[] result;
            //TODO 服务地址硬编码（需要服务注册中心和服务发现机制实现）
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            Object data = rpcResponse.getData();
            Class<?> dataType = rpcResponse.getDataType();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
