package com.escribehost.appointmentcaller.broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableRabbit
public class MessagesConfiguration {

    public static final String APPOINTMENT_REMINDER_BROKER_ERRORS = "appointment-reminder-broker-errors";
    public static final String APPOINTMENT_REMINDER_SUBSCRIBER_LISTENER = "appointmentReminderSubscriberListener";
    private static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    private static final String X_MESSAGE_TTL = "x-message-ttl";
    private static final String X_MAX_PRIORITY = "x-max-priority";
    private final RabbitBrokerProperties rbp;

    public MessagesConfiguration(RabbitBrokerProperties rabbitBrokerProperties) {
        this.rbp = rabbitBrokerProperties;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory appointmentReminderSubscriberContainerFactory(
            ConnectionFactory rabbitConnectionFactory,
            RabbitBrokerProperties rbp) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(rabbitConnectionFactory);
        factory.setTaskExecutor(appointmentReminderTaskExecutor(rbp));
        factory.setMaxConcurrentConsumers(rbp.getAppointmentReminderMaxConcurrentConsumers());
        factory.setConcurrentConsumers(rbp.getAppointmentReminderConcurrentConsumers());
        factory.setErrorHandler(jobErrorHandler());
        factory.setDefaultRequeueRejected(false);
        factory.setPrefetchCount(rbp.getAppointmentReminderPrefetchCount());
        factory.setMessageConverter(producerJackson2JsonMessageConverter());
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Executor appointmentReminderTaskExecutor(RabbitBrokerProperties rbp) {
        return Executors.newFixedThreadPool(rbp.getAppointmentReminderNumberOfThreadsInExecutorThreadPool());
    }

    @Bean
    public ErrorHandler jobErrorHandler() {
        return new ConditionalRejectingErrorHandler(new MessagesConfiguration.AppointmentReminderExceptionStrategy());
    }

    public static class AppointmentReminderExceptionStrategy
            extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {
        private final Logger logger = LoggerFactory.getLogger(APPOINTMENT_REMINDER_BROKER_ERRORS);

        @Override
        public boolean isFatal(Throwable t) {
            if (t instanceof ListenerExecutionFailedException) {
                // TODO: should we do something more ? send email, add some admin notification .... ELK?
                ListenerExecutionFailedException ex = (ListenerExecutionFailedException) t;
                logger.error("Failed to receive and start AppointmentReminder call from "
                        + ex.getFailedMessage().getMessageProperties().getConsumerQueue(), t);
                return true;
            }
            return super.isFatal(t);
        }
    }

    @Bean
    DirectExchange appointmentReminderExchange() {
        return new DirectExchange(rbp.getAppointmentReminderExchange(), true, false);
    }

    @Bean
    public Queue appointmentReminderQueue() {
        return QueueBuilder.durable(rbp.getAppointmentReminderQueue())
                .withArgument(X_DEAD_LETTER_EXCHANGE, rbp.getAppointmentReminderDeadLetterExchange())
                .withArgument(X_MAX_PRIORITY, rbp.getAppointmentReminderMaxPriority())
                .build();
    }

    @Bean
    Binding appointmentReminderExchangeBinding(DirectExchange appointmentReminderExchange, Queue appointmentReminderQueue) {
        return BindingBuilder
                .bind(appointmentReminderQueue)
                .to(appointmentReminderExchange)
                .with(rbp.getAppointmentReminderRoutingKey());
    }

    // dead letter messages

    @Bean
    DirectExchange appointmentReminderDeadLetterExchange() {
        return new DirectExchange(rbp.getAppointmentReminderDeadLetterExchange(), true, false);
    }

    @Bean
    Queue appointmentReminderDeadLetterQueue() {
        return QueueBuilder.durable(rbp.getAppointmentReminderDeadLetterQueue())
                .withArgument(X_DEAD_LETTER_EXCHANGE, rbp.getAppointmentReminderExchange())
                .withArgument(X_MESSAGE_TTL, rbp.getAppointmentReminderDlqMessageTtl())
                .build();
    }

    @Bean
    Binding appointmentReminderDeadLetterExchangeBinding() {
        return BindingBuilder
                .bind(appointmentReminderDeadLetterQueue())
                .to(appointmentReminderDeadLetterExchange())
                .with(rbp.getAppointmentReminderRoutingKey());
    }

    //status queue

    @Bean
    public RabbitTemplate appointmentReminderStatusRabbitTemplate(ConnectionFactory rabbitConnectionFactory,
                                                                  RabbitBrokerProperties rabbitBrokerProperties) {
        RabbitTemplate r = new RabbitTemplate(rabbitConnectionFactory);
        r.setExchange(rabbitBrokerProperties.getAppointmentReminderStatusExchange());
        r.setRoutingKey(rabbitBrokerProperties.getAppointmentReminderStatusRoutingKey());
        r.setMessageConverter(producerJackson2JsonMessageConverter());
        return r;
    }

    @Bean
    DirectExchange appointmentReminderStatusExchange() {
        return new DirectExchange(rbp.getAppointmentReminderStatusExchange(), true, false);
    }

    @Bean
    public Queue appointmentReminderStatusQueue() {
        return QueueBuilder.durable(rbp.getAppointmentReminderStatusQueue())
                .withArgument(X_DEAD_LETTER_EXCHANGE, rbp.getAppointmentReminderStatusDeadLetterExchange())
                .build();
    }

    @Bean
    Binding appointmentReminderStatusExchangeBinding(DirectExchange appointmentReminderStatusExchange, Queue appointmentReminderStatusQueue) {
        return BindingBuilder
                .bind(appointmentReminderStatusQueue)
                .to(appointmentReminderStatusExchange)
                .with(rbp.getAppointmentReminderStatusRoutingKey());
    }

    // dead letter messages

    @Bean
    DirectExchange appointmentReminderStatusDeadLetterExchange() {
        return new DirectExchange(rbp.getAppointmentReminderStatusDeadLetterExchange(), true, false);
    }

    @Bean
    Queue appointmentReminderStatusDeadLetterQueue() {
        return QueueBuilder.durable(rbp.getAppointmentReminderStatusDeadLetterQueue())
                .withArgument(X_DEAD_LETTER_EXCHANGE, rbp.getAppointmentReminderStatusExchange())
                .withArgument(X_MESSAGE_TTL, rbp.getAppointmentReminderStatusDlqMessageTtl())
                .build();
    }

    @Bean
    Binding appointmentReminderStatusDeadLetterExchangeBinding() {
        return BindingBuilder
                .bind(appointmentReminderStatusDeadLetterQueue())
                .to(appointmentReminderStatusDeadLetterExchange())
                .with(rbp.getAppointmentReminderStatusRoutingKey());
    }


}
