package com.yasinbee.chatter.connector.grpc.in;

import com.yasinbee.connector.api.dto.Connect;
import com.yasinbee.connector.api.dto.Message;
import com.yasinbee.connector.api.service.ConnectorServiceGrpc;
import io.grpc.stub.StreamObserver;

public class ConnectorServiceApiGrpc extends ConnectorServiceGrpc.ConnectorServiceImplBase {

    @Override
    public void connectUser(Connect request, StreamObserver<Message> responseObserver) {
        responseObserver.onNext(Message.newBuilder().setText("hello").build());
        responseObserver.onCompleted();
    }
}
