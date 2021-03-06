package com.escribehost.appointmentcaller.broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.escribehost.appointmentcaller.processor.AppointmentReminderProcessor;
import com.escribehost.shared.schedule.reminder.dto.AppointmentReminderCallDto;

import static com.escribehost.appointmentcaller.broker.MessagesConfiguration.APPOINTMENT_REMINDER_SUBSCRIBER_LISTENER;

@Component
public class AppointmentReceiver {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentReceiver.class);
    private AppointmentReminderProcessor appointmentReminderProcessor;

    public AppointmentReceiver(AppointmentReminderProcessor appointmentReminderProcessor) {
        this.appointmentReminderProcessor = appointmentReminderProcessor;
    }

    @RabbitListener(id = APPOINTMENT_REMINDER_SUBSCRIBER_LISTENER,
            containerFactory = "appointmentReminderSubscriberContainerFactory", queues = "#{appointmentReminderQueue}")
    public void receiveMessage(AppointmentReminderCallDto message) {
        logger.info("Receiving message from rabbit,accountId:{}, appointmentReminderId: {}", message.getAccountId(),
                message.getAppointmentReminderId());
        appointmentReminderProcessor.processMessage(message);
    }

}
