package com.escribehost.appointmentcaller.api;

import com.escribehost.appointmentcaller.processor.AppointmentReminderProcessor;
import com.escribehost.shared.schedule.reminder.dto.AppointmentReminderCallDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointment-reminder")
public class AppointmentsCallController {
    private AppointmentReminderProcessor appointmentReminderProcessor;

    @Autowired
    public AppointmentsCallController(AppointmentReminderProcessor appointmentReminderProcessor) {
        this.appointmentReminderProcessor = appointmentReminderProcessor;
    }

    @PostMapping("/")
    public void processAppointmentReminder(@RequestBody AppointmentReminderCallDto appointmentReminderCallDto) {
        appointmentReminderProcessor.processMessage(appointmentReminderCallDto);
    }
}
