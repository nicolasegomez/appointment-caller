package com.escribehost.appointmentcaller.phone;

import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.TwiML;

import java.net.URISyntaxException;

public interface PhoneCaller {

    void call(CallData callData) throws URISyntaxException;

    TwiML getWelcomeDialog(String callSid);

    TwiML handleResponse(String callSid, String digits);

    String getCallMessage(CallData call, String templateFileName);

    void removeCall(CallData callData);

    void handleStatus(String callSid, Call.Status callStatus, String called);
}
