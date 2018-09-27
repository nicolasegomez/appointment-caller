package com.escribehost.appointmentcaller.processor;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.escribehost.appointmentcaller.broker.AppointmentReminderPublisher;
import com.escribehost.appointmentcaller.broker.AppointmentReminderStatusPublisher;
import com.escribehost.appointmentcaller.model.CallData;
import com.escribehost.appointmentcaller.phone.PhoneCaller;
import com.escribehost.appointmentcaller.phone.WrongPhoneNumberException;
import com.escribehost.shared.schedule.reminder.dto.AppointmentReminderCallDto;
import com.escribehost.shared.schedule.reminder.dto.AppointmentReminderStatus;

@Component
public class AppointmentReminderProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentReminderProcessor.class);

    private PhoneCaller phoneCaller;
    private AppointmentReminderStatusPublisher appointmentReminderStatusPublisher;
    private AppointmentReminderPublisher appointmentReminderPublisher;

    @Value("${max.retry.attempts:3}")
    private int maxRetryAttempts;

    public AppointmentReminderProcessor(
            PhoneCaller phoneCaller,
            AppointmentReminderStatusPublisher appointmentReminderStatusPublisher,
            AppointmentReminderPublisher appointmentReminderPublisher) {
        this.phoneCaller = phoneCaller;
        this.appointmentReminderStatusPublisher = appointmentReminderStatusPublisher;
        this.appointmentReminderPublisher = appointmentReminderPublisher;
    }

    public void processMessage(AppointmentReminderCallDto message) {
        if (message != null) {
            try {

                CallData callData = new CallData()
                        .setAppointmentId(message.getAppointmentId())
                        .setAccountId(message.getAccountId())
                        .setAppointmentDate(new SimpleDateFormat("yyyy-MM-dd").parse(message.getAppointmentDate()))
                        .setProvider(
                                message.getProviderFirstName() + " " + message.getProviderMiddleName() + " " + message
                                        .getProviderLastName())
                        .setPatientName(message.getPatientFirstName() + " " + message.getPatientLastName())
                        .setLocationName(message.getLocation())
                        .setPhoneToCall(message.getPhone())
                        .setType(message.getType())
                        .setRoom(message.getRoom());

                phoneCaller.call(callData);
                callData.waitUntilCallEnd();
                phoneCaller.removeCall(callData);

                handleEndedCall(message, callData.getCallEndStatus().orElse(AppointmentReminderStatus.FAILED),
                        callData.getCallId());
            } catch (WrongPhoneNumberException ex) {
                logger.error("Call have failed. AppointmentId: {}, Error: {}", message.getAppointmentId(), ex);
                handleEndedCall(message, AppointmentReminderStatus.WRONG_NUMBER, null);
            } catch (Exception ex) {
                logger.error("Call have failed. AppointmentId: {}, Error: {}", message.getAppointmentId(), ex);
                handleEndedCall(message, AppointmentReminderStatus.FAILED, null);
            }
        } else {
            logger.error("Call have failed, appointment reminder call is null.");
        }
    }

    public void handleEndedCall(AppointmentReminderCallDto appointmentReminderCallDto, AppointmentReminderStatus status,
                                String callId) {
        if (appointmentReminderCallDto.getAttemptNumber() <= maxRetryAttempts &&
                (status == AppointmentReminderStatus.BUSY || status == AppointmentReminderStatus.NO_ANSWER)) {
            logger.warn(
                    "Retrying to call again because appointment reminder status is {}. AppointmentReminderId: {}, " +
                            "attempt number: {}",
                    status, appointmentReminderCallDto.getAppointmentReminderId(),
                    appointmentReminderCallDto.getAttemptNumber() + 1);
            appointmentReminderCallDto.setAttemptNumber(appointmentReminderCallDto.getAttemptNumber() + 1);
            appointmentReminderPublisher.publish(appointmentReminderCallDto);
        } else {
            logger.info("Publishing appointment reminder status response. AppointmentReminderId: {}, status: {}",
                    appointmentReminderCallDto.getAppointmentReminderId(), status);
            appointmentReminderCallDto.setCallId(callId);
            appointmentReminderCallDto.setStatus(status);
            appointmentReminderStatusPublisher.publish(appointmentReminderCallDto);
        }
    }
}
