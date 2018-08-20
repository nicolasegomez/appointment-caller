package com.escribehost.appointmentcaller.api;

import com.escribehost.appointmentcaller.phone.PhoneCaller;
import com.twilio.rest.api.v2010.account.Call;
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
    public String index(@RequestParam("CallSid") String callSid, @RequestParam Map<String, String> params) {
        return phoneCaller.getWelcomeDialog(callSid).toXml();
    }

    @PostMapping(value = "/patient-response", produces = "application/xml")
    public String confirm(@RequestParam("CallSid") String callSid, @RequestParam("Digits") String digits) {
        return phoneCaller.handleResponse(callSid, digits).toXml();
    }

    @PostMapping(value = "/status")
    public void callStatus(@RequestParam("CallSid") String callSid, @RequestParam("CallStatus") Call.Status callStatus,
                           @RequestParam("Called") String called) {
        phoneCaller.handleStatus(callSid, callStatus, called);
    }
}
