package com.escribehost.appointmentcaller.phone;

import com.twilio.twiml.TwiML;

import java.net.URISyntaxException;

public interface PhoneCaller {
    void call() throws URISyntaxException;

    TwiML getWelcomeDialog();

    TwiML handleResponse(String digits);
}
