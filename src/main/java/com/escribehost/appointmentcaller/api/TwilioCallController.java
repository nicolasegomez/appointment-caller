package com.escribehost.appointmentcaller.api;

import com.escribehost.appointmentcaller.phone.PhoneCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/call")
public class TwilioCallController {
    private PhoneCaller phoneCaller;

    @Autowired
    public TwilioCallController(PhoneCaller phoneCaller) {
        this.phoneCaller = phoneCaller;
    }

    @PostMapping(produces = "application/xml")
    public String index(@RequestParam Map<String, String> params) {
        System.out.println("params1: "+params.toString());
        return phoneCaller.getWelcomeDialog().toXml();
    }

    @PostMapping(value = "/patient-response", produces = "application/xml")
    public String confirm(@RequestParam("Digits") String digits, @RequestParam Map<String, String> params) {
        System.out.println("params 2: "+params.toString());
        return phoneCaller.handleResponse(digits).toXml();
    }
}
