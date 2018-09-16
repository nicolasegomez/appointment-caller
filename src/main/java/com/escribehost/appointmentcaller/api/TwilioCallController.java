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

    @PostMapping(value = "/reminder", produces = "application/xml")
    public String reminder(@RequestParam("CallSid") String callSid, @RequestParam Map<String, String> params) {
        return phoneCaller.getReminderWelcomeDialog(callSid).toXml();
    }

    @PostMapping(value = "/cancellation", produces = "application/xml")
    public String cancellation(@RequestParam("CallSid") String callSid, @RequestParam Map<String, String> params) {
        return phoneCaller.getCancellationWelcomeDialog(callSid).toXml();
    }

    @PostMapping(value = "/reminder/patient-response", produces = "application/xml")
    public String responseReminder(@RequestParam("CallSid") String callSid, @RequestParam("Digits") String digits) {
        return phoneCaller.handleReminderResponse(callSid, digits).toXml();
    }

    @PostMapping(value = "/cancellation/patient-response", produces = "application/xml")
    public String responseCancellation(@RequestParam("CallSid") String callSid, @RequestParam("Digits") String digits) {
        return phoneCaller.handleCancellationResponse(callSid, digits).toXml();
    }

    @PostMapping(value = "/status")
    public void callStatus(@RequestParam("CallSid") String callSid, @RequestParam("CallStatus") Call.Status callStatus,
                           @RequestParam("Called") String called) {
        phoneCaller.handleStatus(callSid, callStatus, called);
    }
}
