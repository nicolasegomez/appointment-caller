package com.escribehost.appointmentcaller.processor;

import com.escribehost.appointmentcaller.phone.CallData;
import com.escribehost.appointmentcaller.phone.PhoneCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AppointmentReminderProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentReminderProcessor.class);

    private PhoneCaller phoneCaller;

    public AppointmentReminderProcessor(PhoneCaller phoneCaller) {
        this.phoneCaller = phoneCaller;
    }

    public void processMessage(CallData message) {
        if (message != null) {
            try {
                phoneCaller.call(message);
                message.waitUntilCallEnd();
                phoneCaller.removeCall(message);
                handleEndedCall(message, true);
            } catch (Exception ex) {
                logger.error("Call have failed. AppointmentId: {}, Error: {}", message.getAppointmentId(), ex);
                handleEndedCall(message, false);
            }
        } else {
            handleEndedCall(null, false);
        }
    }

    public void handleEndedCall(CallData callData, boolean success) {
        //TODO: enviar msj de respuesta a rabbit
    }
}
