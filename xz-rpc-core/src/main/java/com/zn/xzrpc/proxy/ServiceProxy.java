package com.zn.xzrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zn.xzrpc.RpcApplication;
import com.zn.xzrpc.config.RpcConfig;
import com.zn.xzrpc.constant.RpcConstant;
import com.zn.xzrpc.model.RpcRequest;
import com.zn.xzrpc.model.RpcResponse;
import com.zn.xzrpc.model.ServiceMetaInfo;
import com.zn.xzrpc.register.Registry;
import com.zn.xzrpc.register.RegistryFactory;
import com.zn.xzrpc.serializer.JdkSerializer;
import com.zn.xzrpc.serializer.Serializer;
import com.zn.xzrpc.serializer.SerializerFactory;
import com.zn.xzrpc.spi.RpcSpiLoader;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 服务代理类 (JDK动态代理）
 */
public class ServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        Serializer serializer = new JdkSerializer();
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        System.out.println(proxy.getClass());
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameters(args)
                .parameterTypes(method.getParameterTypes())
                .build();

        try {
            byte[] bytes = serializer.serialize(rpcRequest);
            byte[] result;
            //服务地址硬编码（需要服务注册中心和服务发现机制实现）
//            HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")

            // 从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            // 暂时先取第一个
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
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
