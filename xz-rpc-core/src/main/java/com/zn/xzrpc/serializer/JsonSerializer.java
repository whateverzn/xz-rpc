package com.zn.xzrpc.serializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zn.xzrpc.model.RpcRequest;
import com.zn.xzrpc.model.RpcResponse;

import java.io.IOException;

/**
 * JSON 序列化器
 */
public class JsonSerializer implements Serializer{

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T object) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        T obj = OBJECT_MAPPER.readValue(bytes, type);
        if (obj instanceof RpcRequest) {
            return handleRequest((RpcRequest) obj, type);
        }
        if (obj instanceof RpcResponse) {
            return handleResponse((RpcResponse) obj, type);
        }
        return obj;
    }

    /**
     * 处理Object序列化时原始对象被擦除
     * @param request
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    private <T>T handleRequest(RpcRequest request, Class<T> type) throws IOException {
        Object[] args = request.getParameters();
        Class<?>[] parameterTypes = request.getParameterTypes();
        for (int i = 0; i < args.length; i++) {
            Class<?> clazz = parameterTypes[i];
            if (!clazz.isAssignableFrom(args[i].getClass())) {
                byte[] argBytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                args[i] = OBJECT_MAPPER.readValue(argBytes, clazz);
            }
        }
        return type.cast(request);
    }

    /**
     * 处理Object序列化时原始对象被擦除
     * @param response
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    private <T>T handleResponse(RpcResponse response, Class<T> type) throws IOException {
        // 处理响应数据
        byte[] dataBytes = OBJECT_MAPPER.writeValueAsBytes(response.getData());
        response.setData(OBJECT_MAPPER.readValue(dataBytes, response.getDataType()));
        return type.cast(response);
    }


}
