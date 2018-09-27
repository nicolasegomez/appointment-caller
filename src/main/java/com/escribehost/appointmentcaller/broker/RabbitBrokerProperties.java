package com.escribehost.appointmentcaller.broker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:/opt/escribe/conf/hl7-integration.properties")
public class RabbitBrokerProperties {
    private final String appointmentReminderExchange = "appointment.reminder.exchange";
    private final String appointmentReminderQueue = "appointment.reminder.queue";
    private final String appointmentReminderRoutingKey = "appointment.reminder.routing.key";
    private final String appointmentReminderDeadLetterExchange = "appointment.reminder.dead.letter.exchange";
    private final String appointmentReminderDeadLetterQueue = "appointment.reminder.dead.letter.queue";

    private final String appointmentReminderStatusExchange = "appointment.reminder.status.exchange";
    private final String appointmentReminderStatusQueue = "appointment.reminder.status.queue";
    private final String appointmentReminderStatusRoutingKey = "appointment.reminder.status.routing.key";
    private final String appointmentReminderStatusDeadLetterExchange = "appointment.reminder.status.dead.letter" +
            ".exchange";
    private final String appointmentReminderStatusDeadLetterQueue = "appointment.reminder.status.dead.letter.queue";

    private String prefix = "";

    @Value("${broker.appointment.reminder.dlq.ttl}")
    private Long appointmentReminderDlqMessageTtl;

    @Value("${broker.appointment.reminder.max.priority:2}")
    private Integer appointmentReminderMaxPriority;

    @Value("${broker.appointment.reminder.concurrent.consumers}")
    private Integer appointmentReminderConcurrentConsumers;

    @Value("${broker.appointment.reminder.max.concurrent.consumers}")
    private Integer appointmentReminderMaxConcurrentConsumers;

    @Value("${broker.appointment.reminder.number.of.threads.in.executor.thread.pool}")
    private Integer appointmentReminderNumberOfThreadsInExecutorThreadPool;

    @Value("${broker.appointment.reminder.status.dlq.ttl}")
    private Long appointmentReminderStatusDlqMessageTtl;

    @Value("${broker.appointment.reminder.status.max.concurrent.consumers}")
    private Integer appointmentReminderStatusMaxConcurrentConsumers;

    @Value("${broker.appointment.reminder.status.number.of.threads.in.executor.thread.pool}")
    private Integer appointmentReminderStatusNumberOfThreadsInExecutorThreadPool;

    @Value("${broker.appointment.reminder.prefetch.count:1}")
    private Integer appointmentReminderPrefetchCount;

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getAppointmentReminderMaxConcurrentConsumers() {
        return appointmentReminderStatusMaxConcurrentConsumers;
    }

    public Integer getAppointmentReminderConcurrentConsumers() {
        return appointmentReminderConcurrentConsumers;
    }

    public Integer getAppointmentReminderNumberOfThreadsInExecutorThreadPool() {
        return appointmentReminderNumberOfThreadsInExecutorThreadPool;
    }

    public Integer getAppointmentReminderStatusMaxConcurrentConsumers() {
        return appointmentReminderMaxConcurrentConsumers;
    }

    public Integer getAppointmentReminderStatusNumberOfThreadsInExecutorThreadPool() {
        return appointmentReminderStatusNumberOfThreadsInExecutorThreadPool;
    }

    public String getAppointmentReminderExchange() {
        return prefix + appointmentReminderExchange;
    }

    public String getAppointmentReminderQueue() {
        return prefix + appointmentReminderQueue;
    }

    public String getAppointmentReminderRoutingKey() {
        return prefix + appointmentReminderRoutingKey;
    }

    public String getAppointmentReminderDeadLetterExchange() {
        return prefix + appointmentReminderDeadLetterExchange;
    }

    public Integer getAppointmentReminderMaxPriority() {
        return appointmentReminderMaxPriority;
    }

    public String getAppointmentReminderDeadLetterQueue() {
        return prefix + appointmentReminderDeadLetterQueue;
    }

    public Long getAppointmentReminderDlqMessageTtl() {
        return appointmentReminderDlqMessageTtl * 1000L;
    }

    public String getAppointmentReminderStatusExchange() {
        return appointmentReminderStatusExchange;
    }

    public String getAppointmentReminderStatusQueue() {
        return appointmentReminderStatusQueue;
    }

    public String getAppointmentReminderStatusRoutingKey() {
        return appointmentReminderStatusRoutingKey;
    }

    public String getAppointmentReminderStatusDeadLetterExchange() {
        return appointmentReminderStatusDeadLetterExchange;
    }

    public String getAppointmentReminderStatusDeadLetterQueue() {
        return appointmentReminderStatusDeadLetterQueue;
    }

    public Long getAppointmentReminderStatusDlqMessageTtl() {
        return appointmentReminderStatusDlqMessageTtl;
    }

    public Integer getAppointmentReminderPrefetchCount() {
        return appointmentReminderPrefetchCount;
    }
}
