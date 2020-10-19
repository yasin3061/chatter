package com.yasinbee.chatter.client.message;

import com.yasinbee.chatter.client.grpc.out.GrpcApiClient;
import com.yasinbee.connector.api.dto.Message;
import com.yasinbee.connector.api.service.ConnectorServiceGrpc;

public class MessageForwarder {

    public void forward(Message message) {
        GrpcApiClient client = new GrpcApiClient(ConnectorServiceGrpc.class);
        client.invokeUnaryCall(ConnectorServiceGrpc::getSendMessageMethod, message);
        client.disconnect();
    }
}
