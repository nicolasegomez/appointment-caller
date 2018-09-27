package com.escribehost.appointmentcaller.broker;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.escribehost.shared.schedule.reminder.dto.AppointmentReminderCallDto;

@Component
public class AppointmentReminderStatusPublisher {
    @Autowired
    private RabbitTemplate appointmentReminderStatusRabbitTemplate;

    public void publish(AppointmentReminderCallDto appointmentReminderCallDto) {
        appointmentReminderStatusRabbitTemplate.convertAndSend(appointmentReminderCallDto);
    }
}
