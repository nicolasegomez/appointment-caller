package com.escribehost.appointmentcaller.phone;

import com.escribehost.appointmentcaller.model.CallData;
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
            callData.callEnd(Call.Status.FAILED,"22");
        } else {
            callData.setUserResponse(1);
            callData.callEnd(Call.Status.COMPLETED,"22");
        }
    }

    @Override
    public TwiML getReminderWelcomeDialog(String callSid) {
        return null;
    }

    @Override
    public TwiML getCancellationWelcomeDialog(String callSid) {
        return null;
    }

    @Override
    public TwiML handleReminderResponse(String callSid, String digits) {
        return null;
    }

    @Override
    public TwiML handleCancellationResponse(String callSid, String digits) {
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
