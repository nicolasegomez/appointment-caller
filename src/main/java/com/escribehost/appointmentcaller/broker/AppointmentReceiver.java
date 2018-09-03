package com.escribehost.appointmentcaller.broker;

import com.escribehost.appointmentcaller.phone.CallData;
import com.escribehost.appointmentcaller.processor.AppointmentReminderProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentReceiver {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentReceiver.class);
    private AppointmentReminderProcessor appointmentReminderProcessor;

    public AppointmentReceiver(AppointmentReminderProcessor appointmentReminderProcessor) {
        this.appointmentReminderProcessor = appointmentReminderProcessor;
    }

    @RabbitListener(queues = "${rabbit.queue.name}")
    public void receiveMessage(CallData message) {
        logger.info("Receiving message from rabbit: {}", message.getHospitalName());
        appointmentReminderProcessor.processMessage(message);
    }

}
