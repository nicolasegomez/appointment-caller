package com.escribehost.appointmentcaller.phone;

import com.twilio.rest.api.v2010.account.Call;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Component
@Profile("dev")
public class PhoneCallerMockImpl extends PhoneCallerImpl {

    private static final Logger logger = LoggerFactory.getLogger(PhoneCallerMockImpl.class);

    @Override
    public void call(CallData callData) throws URISyntaxException {
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
