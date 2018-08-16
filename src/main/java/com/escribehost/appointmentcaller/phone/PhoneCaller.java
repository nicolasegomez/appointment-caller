package com.escribehost.appointmentcaller.phone;

import com.twilio.twiml.TwiML;

import java.net.URISyntaxException;

public interface PhoneCaller {

    void call(CallData callData) throws URISyntaxException;

    TwiML getWelcomeDialog(String callSid);

    TwiML handleResponse(String callSid, String digits);

    String getCallMessage(CallData call, String templateFileName);
}
