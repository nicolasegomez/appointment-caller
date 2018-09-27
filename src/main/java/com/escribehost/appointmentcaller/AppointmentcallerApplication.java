package com.escribehost.appointmentcaller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.escribehost.appointmentcaller.broker.MessagesConfiguration;

@SpringBootApplication
@Import(MessagesConfiguration.class)
public class AppointmentcallerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppointmentcallerApplication.class, args);
    }

}
