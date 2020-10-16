package com.yasinbee.chatter.connector;

import com.yasinbee.chatter.connector.grpc.in.GrpcApiServer;
import org.springframework.stereotype.Service;

@Service
public class AppLauncher {

    private final GrpcApiServer grpcApiServer;

    public AppLauncher(GrpcApiServer grpcApiServer) {
        this.grpcApiServer = grpcApiServer;
    }

    public void launch() {
        grpcApiServer.start();
    }
}
