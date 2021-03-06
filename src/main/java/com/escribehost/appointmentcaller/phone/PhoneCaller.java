package com.escribehost.appointmentcaller.phone;

import com.escribehost.appointmentcaller.model.CallData;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.TwiML;

public interface PhoneCaller {

    void call(CallData callData);

    TwiML getReminderWelcomeDialog(String callSid);

    TwiML getCancellationWelcomeDialog(String callSid);

    TwiML handleReminderResponse(String callSid, String digits);

    TwiML handleCancellationResponse(String callSid, String digits);

    void removeCall(CallData callData);

    void handleStatus(String callSid, Call.Status callStatus, String called);
}
