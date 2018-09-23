package com.escribehost.appointmentcaller.processor;

import com.escribehost.appointmentcaller.broker.AppointmentReminderStatusPublisher;
import com.escribehost.appointmentcaller.model.CallData;
import com.escribehost.appointmentcaller.phone.PhoneCaller;
import com.escribehost.appointmentcaller.phone.WrongPhoneNumberException;
import com.escribehost.shared.schedule.reminder.dto.AppointmentReminderCallDto;
import com.escribehost.shared.schedule.reminder.dto.AppointmentReminderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class AppointmentReminderProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentReminderProcessor.class);

    private PhoneCaller phoneCaller;
    private AppointmentReminderStatusPublisher appointmentReminderStatusPublisher;

    public AppointmentReminderProcessor(PhoneCaller phoneCaller, AppointmentReminderStatusPublisher appointmentReminderStatusPublisher) {
        this.phoneCaller = phoneCaller;
        this.appointmentReminderStatusPublisher = appointmentReminderStatusPublisher;
    }

    public void processMessage(AppointmentReminderCallDto message) {
        if (message != null) {
            try {

                CallData callData = new CallData()
                        .setAppointmentId(message.getAppointmentId())
                        .setAccountId(message.getAccountId())
                        .setAppointmentDate(new SimpleDateFormat("yyyy-MM-dd").parse(message.getAppointmentDate()))
                        .setProvider(message.getProviderFirstName() + " " + message.getProviderMiddleName() + " " + message.getProviderLastName())
                        .setPatientName(message.getPatientFirstName() + " " + message.getPatientLastName())
                        .setLocationName(message.getLocation())
                        .setPhoneToCall(message.getPhone())
                        .setType(message.getType())
                        .setRoom(message.getRoom());


                phoneCaller.call(callData);
                callData.waitUntilCallEnd();
                phoneCaller.removeCall(callData);

                handleEndedCall(message, callData.getCallEndStatus().orElse(AppointmentReminderStatus.FAILED), callData.getCallId());
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

    public void handleEndedCall(AppointmentReminderCallDto appointmentReminderCallDto, AppointmentReminderStatus status, String callId) {
        logger.info("Publishing appointment reminder status response. AppointmentReminderId: {}, status: {}", appointmentReminderCallDto.getAppointmentReminderId(), status);
        appointmentReminderCallDto.setCallId(callId);
        appointmentReminderCallDto.setStatus(status);
        appointmentReminderStatusPublisher.publish(appointmentReminderCallDto);
    }
}
