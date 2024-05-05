package com.zn.xzrpc.model;

import com.zn.xzrpc.RpcApplication;
import com.zn.xzrpc.register.LocalRegister;
import com.zn.xzrpc.serializer.JdkSerializer;
import com.zn.xzrpc.serializer.Serializer;
import com.zn.xzrpc.serializer.SerializerFactory;
import com.zn.xzrpc.spi.RpcSpiLoader;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import jdk.nashorn.internal.ir.CallNode;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Vertx请求服务处理器
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {
        //spi机制获取序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        //打印日志
        System.out.println("Received request: " + request.method() + " " + request.uri());

        //异步处理HTTP请求
        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //构造响应对象
            RpcResponse rpcResponse = new RpcResponse();
            if (rpcRequest == null) {
                rpcResponse.setMessage("RpcRequest is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }
            try {
                //获取注册服务类
                Class<?> implService = LocalRegister.getService(rpcRequest.getServiceName());
                //反射执行方法
                Method method = implService.getMethod(rpcRequest.getMethodName(),
                        rpcRequest.getParameterTypes());
                Object obj = implService.newInstance();
                Object result = method.invoke(obj, rpcRequest.getParameters());
                //封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("invoke successful");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            //响应
            doResponse(request, rpcResponse, serializer);
        });
    }

    /**
     * 响应
     *
     * @param request
     * @param rpcResponse
     * @param serializer
     */
    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request
                .response()
                .putHeader("content-type", "application/json");
        try {
            byte[] bytes = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }

}
