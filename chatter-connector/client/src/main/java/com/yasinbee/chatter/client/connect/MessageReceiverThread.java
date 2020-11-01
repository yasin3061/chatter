package com.yasinbee.chatter.client.connect;

import com.yasinbee.connector.api.dto.User;
import com.yasinbee.connector.api.service.ConnectorService;

public class MessageReceiverThread implements Runnable {

    private final ConnectorService connector;
    private final User user;

    public MessageReceiverThread(ConnectorService connector, User user) {
        this.connector = connector;
        this.user = user;
    }

    @Override
    public void run() {
        connector.connectUser(user).forEachRemaining(m -> System.out.println("New " +
                "message from: " +
                m.getFrom() + ", " + m.getText()));
        System.out.println("User disconnected");
    }

    public void startThread() {
        Thread t = new Thread(this);
        t.start();
    }
}
