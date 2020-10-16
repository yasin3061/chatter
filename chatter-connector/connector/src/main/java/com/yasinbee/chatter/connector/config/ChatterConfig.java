package com.yasinbee.chatter.connector.config;

import com.yasinbee.chatter.connector.grpc.in.ConnectorServiceApiGrpc;
import com.yasinbee.chatter.connector.grpc.in.GrpcApiServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatterConfig {

    @Bean
    public ConnectorServiceApiGrpc getConnectorServiceApiGrpc() {
        return new ConnectorServiceApiGrpc();
    }

    @Bean
    public GrpcApiServer getGrpcApiServer(ConnectorServiceApiGrpc connectorServiceApiGrpc) {
        return new GrpcApiServer(connectorServiceApiGrpc);
    }
}
