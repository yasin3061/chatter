package com.yasinbee.connector.api.service;

import com.yasinbee.connector.api.dto.Disconnect;
import com.yasinbee.connector.api.dto.Message;
import com.yasinbee.connector.api.dto.MessageResponse;
import com.yasinbee.connector.api.dto.User;

import java.util.Iterator;

public interface ConnectorService {
    Iterator<Message> connectUser(User user);
    MessageResponse sendMessage(Message message);
    Disconnect disconnectUser(User user);
}
