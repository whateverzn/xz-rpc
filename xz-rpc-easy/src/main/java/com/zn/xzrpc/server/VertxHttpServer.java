package com.zn.xzrpc.server;

import io.vertx.core.Vertx;

/**
 * Vertx服务器
 */
public class VertxHttpServer implements HttpServer {

    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();
        server.requestHandler(request -> {
            System.out.println("Received request: " + request.method() + " " + request.uri());
            request.response()
                    .putHeader("Content-Type", "text/plain")
                    .end();
        });
        server.listen(port, res -> {
            if (res.succeeded()) {
                System.out.println("Vertx server started successfully, listening on port " + port);
            } else {
                System.out.println("Failed to start server");
            }
        });
    }
}
