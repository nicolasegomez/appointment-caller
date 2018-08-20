package com.escribehost.appointmentcaller;

import com.escribehost.appointmentcaller.messages.AppointmentReceiver;
import com.escribehost.appointmentcaller.messages.MessagesConfiguration;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MessagesConfiguration.class)
public class AppointmentcallerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppointmentcallerApplication.class, args);
    }

}
