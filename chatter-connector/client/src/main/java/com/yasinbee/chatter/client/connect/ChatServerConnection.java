package com.yasinbee.chatter.client.connect;

import com.yasinbee.chatter.client.grpc.out.GrpcApiClient;
import com.yasinbee.connector.api.dto.Message;
import com.yasinbee.connector.api.dto.User;
import com.yasinbee.connector.api.service.ConnectorServiceGrpc;

import java.util.Iterator;

public class ChatServerConnection implements Runnable {

    private final User connectRequest;
    private GrpcApiClient connectionClient;

    public ChatServerConnection(User connect) {
        this.connectRequest = connect;
    }

    private Iterator<Message> connect(User connectRequest) {
        connectionClient = new GrpcApiClient(ConnectorServiceGrpc.class);

        return (Iterator<Message>)connectionClient.invokeServerStreamingCall(ConnectorServiceGrpc::getConnectUserMethod,
                connectRequest);
    }

    @Override
    public void run() {
        connect(connectRequest).forEachRemaining(m -> System.out.println("New message from: " +
                m.getFrom() + ", " + m.getText()));
        System.out.println("User disconnected");
    }

    public void connect() {
        Thread t = new Thread(this);
        t.start();
    }

    public void disconnect() {
        connectionClient.invokeUnaryCall(ConnectorServiceGrpc::getDisconnectUserMethod,
                connectRequest);
        connectionClient.disconnect();
    }
}
