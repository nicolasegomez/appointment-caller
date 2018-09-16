package com.escribehost.appointmentcaller.processor;

import com.escribehost.appointmentcaller.broker.AppointmentReminderStatusPublisher;
import com.escribehost.appointmentcaller.model.AppointmentReminderCall;
import com.escribehost.appointmentcaller.model.AppointmentReminderStatus;
import com.escribehost.appointmentcaller.model.CallData;
import com.escribehost.appointmentcaller.phone.PhoneCaller;
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

    public void processMessage(AppointmentReminderCall message) {
        if (message != null) {
            try {

                CallData callData = new CallData()
                        .setAppointmentId(message.getAppointmentId())
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

                handleEndedCall(message, AppointmentReminderStatus.COMPLETED);
            } catch (Exception ex) {
                logger.error("Call have failed. AppointmentId: {}, Error: {}", message.getAppointmentId(), ex);
                handleEndedCall(message, AppointmentReminderStatus.FAILED);
            }
        } else {
            logger.error("Call have failed, appointment reminder call is null.");
        }
    }

    public void handleEndedCall(AppointmentReminderCall appointmentReminderCall, AppointmentReminderStatus status) {
        logger.info("Publishing appointment reminder status response. AppointmentReminderId: {}, status: {}", appointmentReminderCall.getAppointmentReminderId(), status);
        appointmentReminderCall.setStatus(status);
        appointmentReminderStatusPublisher.publish(appointmentReminderCall);
    }
}
