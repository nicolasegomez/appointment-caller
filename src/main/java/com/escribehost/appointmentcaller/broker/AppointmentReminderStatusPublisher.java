package com.escribehost.appointmentcaller.broker;

import com.escribehost.appointmentcaller.model.AppointmentReminderCall;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class AppointmentReminderStatusPublisher {
    @Autowired
    private RabbitTemplate appointmentReminderStatusRabbitTemplate;

    public void publish(AppointmentReminderCall appointmentReminderCall) {
        appointmentReminderStatusRabbitTemplate.convertAndSend(appointmentReminderCall);
    }
}
