package com.escribehost.appointmentcaller.phone.twilio;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.escribehost.appointmentcaller.model.CallData;
import com.twilio.rest.api.v2010.account.Call;

@Component
@Profile("dev")
public class PhoneCallerTwilioMock extends PhoneCallerTwilio {

    private static final Logger logger = LoggerFactory.getLogger(PhoneCallerTwilioMock.class);

    public PhoneCallerTwilioMock(TwilioConfig twilioConfig) {
        super(twilioConfig);
    }

    @Override
    public void call(CallData callData) {
        String callSid = UUID.randomUUID().toString();
        logger.info("DEV - Call starting {}", callSid);
        currentCalls.put(callSid, callData);
        Timer timer = new Timer();
        TimerTask delayedTask = new TimerTask() {
            public void run() {
                handleStatus(callSid, Call.Status.COMPLETED, "1155632686");
            }
        };
        timer.schedule(delayedTask, 10000);
    }

}
