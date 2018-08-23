package com.escribehost.appointmentcaller.phone;

import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.TwiML;
import org.assertj.core.util.Lists;

import java.net.URISyntaxException;
import java.util.List;

public class PhoneCallerMock implements PhoneCaller {

    private List<Integer> failedAppointmentIds;

    public PhoneCallerMock() {
        this(Lists.emptyList());
    }

    public PhoneCallerMock(List<Integer> failedAppointmentIds) {
        this.failedAppointmentIds = failedAppointmentIds;
    }

    @Override
    public void call(CallData callData) throws URISyntaxException {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.failedAppointmentIds.contains(callData.getAppointmentId())) {
            callData.callEnd(Call.Status.FAILED);
        } else {
            callData.setUserResponse(1);
            callData.callEnd(Call.Status.COMPLETED);
        }
    }

    @Override
    public TwiML getWelcomeDialog(String callSid) {
        return null;
    }

    @Override
    public TwiML handleResponse(String callSid, String digits) {
        return null;
    }

    @Override
    public String getCallMessage(CallData call, String templateFileName) {
        return null;
    }

    @Override
    public void removeCall(CallData callData) {
    }

    @Override
    public void handleStatus(String callSid, Call.Status callStatus, String called) {
    }
}