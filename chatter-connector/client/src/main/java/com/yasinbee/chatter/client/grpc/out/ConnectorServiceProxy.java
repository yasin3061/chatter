package com.yasinbee.chatter.client.grpc.out;

import com.yasinbee.connector.api.dto.Disconnect;
import com.yasinbee.connector.api.dto.Message;
import com.yasinbee.connector.api.dto.MessageResponse;
import com.yasinbee.connector.api.dto.User;
import com.yasinbee.connector.api.service.ConnectorService;
import com.yasinbee.connector.api.service.ConnectorServiceGrpc;

import java.util.Iterator;

public class ConnectorServiceProxy implements ConnectorService {

    private GrpcApiClient connectionClient;

    @Override
    public Iterator<Message> connectUser(User user) {
        connectionClient = new GrpcApiClient(ConnectorServiceGrpc.class);

        return (Iterator<Message>) connectionClient.invokeServerStreamingCall(ConnectorServiceGrpc::getConnectUserMethod,
                user);
    }

    @Override
    public MessageResponse sendMessage(Message message) {
        GrpcApiClient client = new GrpcApiClient(ConnectorServiceGrpc.class);
        MessageResponse messageResponse =
                (MessageResponse) client.invokeUnaryCall(ConnectorServiceGrpc::getSendMessageMethod,
                        message);
        client.disconnect();
        return messageResponse;
    }

    @Override
    public MessageResponse deliverMessage(Message message) {
        return null;
    }

    @Override
    public Disconnect disconnectUser(User user) {
        Disconnect disconnectResponse =
                (Disconnect) connectionClient.invokeUnaryCall(ConnectorServiceGrpc::getDisconnectUserMethod,
                user);
        connectionClient.disconnect();
        return disconnectResponse;
    }
}
