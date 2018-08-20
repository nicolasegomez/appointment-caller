package com.escribehost.appointmentcaller.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AppointmentReceiver {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentReceiver.class);

    public void receiveMessage(String message) {
        logger.info("Receiving message from rabbit: {}",message);
    }
}
