package com.yasinbee.chatter.client.connect;

import com.yasinbee.connector.api.dto.User;
import com.yasinbee.connector.api.service.ConnectorService;

public class ShutdownHook implements Runnable {

    private final ConnectorService connectorService;
    private final User user;

    public ShutdownHook(ConnectorService connectorService, User user) {
        this.connectorService = connectorService;
        this.user = user;
    }

    @Override
    public void run() {
        connectorService.disconnectUser(user);
    }

    public void register() {
        Thread t = new Thread(this);
        Runtime.getRuntime().addShutdownHook(t);
    }
}
