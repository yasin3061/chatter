package com.yasinbee.connector.api.service;

import com.yasinbee.connector.api.dto.Message;
import com.yasinbee.connector.api.dto.MessageResponse;

public interface DeliveryService {
    MessageResponse deliverMessage(Message message);
}
