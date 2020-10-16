package com.yasinbee.chatter.connector.grpc.in;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;

import java.io.IOException;

public class GrpcApiServer {

    private final BindableService service;
    private Server server;

    public GrpcApiServer(BindableService service) {
        this.service = service;
        registerService();
    }

    private void registerService() {
        ServerServiceDefinition serverServiceDefinition = service.bindService();
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(50000);
        server = serverBuilder.addService(serverServiceDefinition).build();
    }

    public void start() {
        if (server == null) {
            throw new IllegalStateException("Server is not yet initialized");
        }

        try {
            server.start();
            awaitTermination();
        } catch (IOException e) {
            throw new RuntimeException("Could not start the gRPC server", e);
        }
    }

    private void awaitTermination() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down the server");
            server.shutdown();
        }));

        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
