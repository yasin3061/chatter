package com.yasinbee.chatter.connector.grpc.in;

import com.yasinbee.connector.api.dto.Disconnect;
import com.yasinbee.connector.api.dto.Message;
import com.yasinbee.connector.api.dto.MessageResponse;
import com.yasinbee.connector.api.dto.User;
import com.yasinbee.connector.api.service.ConnectorServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectorServiceApiGrpc extends ConnectorServiceGrpc.ConnectorServiceImplBase {

    Map<String, StreamObserver<Message>> userConnections = new HashMap<>();
    Map<String, List<Message>> pendingMessages = new HashMap<>();

    @Override
    public void connectUser(User request, StreamObserver<Message> responseObserver) {
        try {
            String currentUser = request.getId();
            userConnections.put(currentUser, responseObserver);

            String response =
                    "User " + currentUser + " " +
                            "connected.";
            System.out.println(response);
            Message.Builder messageBuilder = Message.newBuilder().setFrom("system")
                    .setTo(currentUser);
            responseObserver.onNext(messageBuilder.setText(response).build());
            sendPendingMessagesIfExists(responseObserver, currentUser, messageBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(Message request, StreamObserver<MessageResponse> responseObserver) {
        try {
            StreamObserver<Message> userConnection = userConnections.get(request.getTo());
            if (userConnection != null) {
                userConnection.onNext(request);
            } else {
                saveMessagesAsPending(request);
            }
            responseObserver.onNext(MessageResponse.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnectUser(User request, StreamObserver<Disconnect> responseObserver) {
        StreamObserver<Message> userConnection = userConnections.get(request.getId());
        if (userConnection != null) {
            userConnection.onCompleted();
            userConnections.remove(request.getId());
            System.out.println("User " + request.getId() + " disconnected");
        }

        responseObserver.onNext(Disconnect.newBuilder().build());
        responseObserver.onCompleted();
    }

    private void sendPendingMessagesIfExists(StreamObserver<Message> responseObserver,
                                             String currentUser, Message.Builder messageBuilder) {
        if (pendingMessages.get(currentUser) != null) {

            responseObserver.onNext(messageBuilder
                    .setText("Receiving pending messages").build());
            pendingMessages.get(currentUser).forEach(responseObserver::onNext);
            responseObserver.onNext(messageBuilder
                    .setText("All pending messages received").build());
        }
    }

    private void saveMessagesAsPending(Message request) {
        List<Message> messages = pendingMessages.get(request.getTo());
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(request);
        pendingMessages.put(request.getTo(), messages);
    }
}
