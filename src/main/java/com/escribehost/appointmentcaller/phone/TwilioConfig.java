package com.escribehost.appointmentcaller.phone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class TwilioConfig {

    @Autowired
    private Environment env;

    public String getAccountSid(Long accountId) {
        return env.getProperty(MessageFormat.format("twilio.{0}.account.sid", accountId), "twilio.account.sid");
    }

    public String getToken(Long accountId) {
        return env.getProperty(MessageFormat.format("twilio.{0}.auth.token", accountId), "twilio.auth.token");
    }

    public String getPhoneFrom(Long accountId) {
        return env.getProperty(MessageFormat.format("twilio.{0}.phone.from", accountId), "twilio.phone.from");
    }
}
