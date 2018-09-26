package com.escribehost.appointmentcaller.phone.twilio;

import com.escribehost.appointmentcaller.model.CallData;
import com.escribehost.shared.schedule.reminder.dto.AppointmentReminderStatus;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.TwiML;
import com.twilio.twiml.voice.Say;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class PhoneCallerTwilioTest {
    private static String CALL_ID = "123";
    private PhoneCallerTwilio phoneCallerTwilio;
    private TwilioConfig twilioConfig;

    @Before
    public void setUp() {
        twilioConfig = mock(TwilioConfig.class);
        phoneCallerTwilio = new PhoneCallerTwilio(twilioConfig);
    }

    @Test
    public void test_getReminderWelcomeDialog() {
        // given
        CallData callData = buildCallData();
        phoneCallerTwilio.currentCalls.put(CALL_ID, callData);
        String message = phoneCallerTwilio.getCallMessage(callData, "templates/reminder-welcome.twig");

        // when
        TwiML response = phoneCallerTwilio.getReminderWelcomeDialog(CALL_ID);

        // then
        assertThat(((Say) response.getChildren().get(0).getChildren().get(0)).getMessage()).isEqualTo(message);
    }

    @Test
    public void test_getCancellationWelcomeDialog() {
        // given
        CallData callData = buildCallData();
        phoneCallerTwilio.currentCalls.put(CALL_ID, callData);
        String message = phoneCallerTwilio.getCallMessage(callData, "templates/cancellation-welcome.twig");

        // when
        TwiML response = phoneCallerTwilio.getCancellationWelcomeDialog(CALL_ID);

        // then
        assertThat(((Say) response.getChildren().get(0).getChildren().get(0)).getMessage()).isEqualTo(message);
    }

    @Test
    public void test_handleReminderResponse_digit1() {
        // given
        CallData callData = buildCallData();
        phoneCallerTwilio.currentCalls.put(CALL_ID, callData);
        String message = phoneCallerTwilio.getCallMessage(callData, "templates/reminder-confirm.twig");

        // when
        TwiML response = phoneCallerTwilio.handleReminderResponse(CALL_ID, "1");

        // then
        assertThat(((Say) response.getChildren().get(0)).getMessage()).isEqualTo(message);
    }

    @Test
    public void test_handleReminderResponse_digit2() {
        // given
        CallData callData = buildCallData();
        phoneCallerTwilio.currentCalls.put(CALL_ID, callData);
        String message = phoneCallerTwilio.getCallMessage(callData, "templates/cancel.twig");

        // when
        TwiML response = phoneCallerTwilio.handleReminderResponse(CALL_ID, "2");

        // then
        assertThat(((Say) response.getChildren().get(0)).getMessage()).isEqualTo(message);
    }

    @Test
    public void test_handleReminderResponse_digit_no_choice() {
        // given
        CallData callData = buildCallData();
        phoneCallerTwilio.currentCalls.put(CALL_ID, callData);
        String message = phoneCallerTwilio.getCallMessage(callData, "templates/nochoice.twig");

        // when
        TwiML response = phoneCallerTwilio.handleReminderResponse(CALL_ID, "No choice");

        // then
        assertThat(((Say) response.getChildren().get(0)).getMessage()).isEqualTo(message);
    }

    @Test
    public void test_handleCancellationResponse_digit_1() {
        // given
        CallData callData = buildCallData();
        phoneCallerTwilio.currentCalls.put(CALL_ID, callData);
        String message = phoneCallerTwilio.getCallMessage(callData, "templates/cancellation-reschedule.twig");

        // when
        TwiML response = phoneCallerTwilio.handleCancellationResponse(CALL_ID, "1");

        // then
        assertThat(((Say) response.getChildren().get(0)).getMessage()).isEqualTo(message);
    }

    @Test
    public void test_handleStatus_completed() {
        // given
        CallData callData = buildCallData();
        phoneCallerTwilio.currentCalls.put(CALL_ID, callData);
        String message = phoneCallerTwilio.getCallMessage(callData, "templates/cancellation-reschedule.twig");

        // when
        phoneCallerTwilio.handleStatus(CALL_ID, Call.Status.COMPLETED, "PhoneNumber");

        // then
        assertThat(callData.getCallEndStatus().get()).isEqualTo(AppointmentReminderStatus.COMPLETED);
        assertThat(callData.isCallFinished()).isTrue();
    }

    @Test
    public void test_handleStatus_busy() {
        // given
        CallData callData = buildCallData();
        phoneCallerTwilio.currentCalls.put(CALL_ID, callData);
        String message = phoneCallerTwilio.getCallMessage(callData, "templates/cancellation-reschedule.twig");

        // when
        phoneCallerTwilio.handleStatus(CALL_ID, Call.Status.BUSY, "PhoneNumber");

        // then
        assertThat(callData.getCallEndStatus().get()).isEqualTo(AppointmentReminderStatus.BUSY);
        assertThat(callData.isCallFinished()).isTrue();
    }

    @Test
    public void test_handleStatus_failed() {
        // given
        CallData callData = buildCallData();
        phoneCallerTwilio.currentCalls.put(CALL_ID, callData);
        String message = phoneCallerTwilio.getCallMessage(callData, "templates/cancellation-reschedule.twig");

        // when
        phoneCallerTwilio.handleStatus(CALL_ID, Call.Status.FAILED, "PhoneNumber");

        // then
        assertThat(callData.getCallEndStatus().get()).isEqualTo(AppointmentReminderStatus.FAILED);
        assertThat(callData.isCallFinished()).isTrue();
    }

    @Test
    public void test_handleStatus_in_progress() {
        // given
        CallData callData = buildCallData();
        phoneCallerTwilio.currentCalls.put(CALL_ID, callData);
        String message = phoneCallerTwilio.getCallMessage(callData, "templates/cancellation-reschedule.twig");

        // when
        phoneCallerTwilio.handleStatus(CALL_ID, Call.Status.IN_PROGRESS, "PhoneNumber");

        // then
        assertThat(callData.getCallEndStatus()).isNotPresent();
        assertThat(callData.isCallFinished()).isFalse();
    }

    private CallData buildCallData() {
        return new CallData()
                .setProvider("Provider")
                .setLocationName("Location")
                .setPhoneToCall("Phone")
                .setAppointmentDate(new Date())
                .setPatientName("Patient")
                .setAppointmentId(1L);
    }
}
