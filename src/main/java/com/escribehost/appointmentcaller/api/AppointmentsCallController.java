package com.escribehost.appointmentcaller.api;

import com.escribehost.appointmentcaller.model.AppointmentReminderCall;
import com.escribehost.appointmentcaller.processor.AppointmentReminderProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointment-reminder")
public class AppointmentsCallController {
    private AppointmentReminderProcessor appointmentReminderProcessor;

    @Autowired
    public AppointmentsCallController(AppointmentReminderProcessor appointmentReminderProcessor) {
        this.appointmentReminderProcessor = appointmentReminderProcessor;
    }

    @PostMapping("/")
    public void processAppointmentReminder(@RequestBody AppointmentReminderCall appointmentReminderCall) {
        appointmentReminderProcessor.processMessage(appointmentReminderCall);
    }

}
