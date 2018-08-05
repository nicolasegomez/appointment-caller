package com.escribehost.appointmentcaller.api;

import com.escribehost.appointmentcaller.phone.PhoneCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("/call")
public class TwilioCallController {
    private PhoneCaller phoneCaller;

    @Autowired
    public TwilioCallController(PhoneCaller phoneCaller) {
        this.phoneCaller = phoneCaller;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void index() {
        phoneCaller.getWelcomeDialog();
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public void confirm() {
        phoneCaller.getConfirmDialog();
    }

    @RequestMapping(value = "/reschedule", method = RequestMethod.POST)
    public void reschedule() {
        phoneCaller.getRescheduleDialog();
    }
}
