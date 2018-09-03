package com.escribehost.appointmentcaller.broker;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageTestConfiguration {

    private String appointmentQueueName = "appointment-queuel";
    private String routingKey = "twilio.#";
    private String topicExchangeName = "appointment-confirmation-call";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        //The routing key is set to the name of the queue by the broker for the default exchange.
        template.setRoutingKey(this.routingKey);
        //Where we will synchronously receive broker from
        template.setQueue(this.appointmentQueueName);
        template.setExchange(this.topicExchangeName);
        return template;
    }

    @Bean
    // Every queue is bound to the default direct exchange
    public Queue appointmentQueue() {
        return new Queue(this.appointmentQueueName);
    }

}
