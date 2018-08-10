package com.escribehost.appointmentcaller.api;

import com.escribehost.appointmentcaller.appoiment.AppointmentJobCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppointmentsCallController {

    private AppointmentJobCreator appointmentJobCreator;

    @Autowired
    public AppointmentsCallController(AppointmentJobCreator appointmentJobCreator) {
        this.appointmentJobCreator = appointmentJobCreator;
    }

    @RequestMapping(value = "/confirmation-calls", method = RequestMethod.POST)
    public void confirmationCalls() {
        appointmentJobCreator.createConfirmationCallJobs();
    }

    @RequestMapping(value = "/reschedule-calls", method = RequestMethod.POST)
    public void rescheduleCalls() {
        appointmentJobCreator.createRescheduleCallJobs();
    }

}
