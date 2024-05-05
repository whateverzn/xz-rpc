package com.zn.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zn.example.common.model.User;
import com.zn.example.common.service.UserService;
import com.zn.xzrpc.model.RpcRequest;
import com.zn.xzrpc.model.RpcResponse;
import com.zn.xzrpc.serializer.JdkSerializer;
import com.zn.xzrpc.serializer.Serializer;

import java.io.IOException;

/**
 * 静态代理实现服务远程调用
 */
public class UserServiceProxy implements UserService {

    @Override
    public User getUser(User user) {
        Serializer serializer = new JdkSerializer();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameters(new User[]{user})
                .parameterTypes(new Class<?>[]{User.class})
                .build();

        try {
            byte[] bytes = serializer.serialize(rpcRequest);
            byte[] result;
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponose = serializer.deserialize(result, RpcResponse.class);
            Object data = rpcResponose.getData();
            Class<?> dataType = rpcResponose.getDataType();
            return (User) dataType.cast(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
