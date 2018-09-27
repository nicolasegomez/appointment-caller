package com.escribehost.appointmentcaller.processor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import com.escribehost.appointmentcaller.broker.AppointmentReminderPublisher;
import com.escribehost.appointmentcaller.broker.AppointmentReminderStatusPublisher;
import com.escribehost.appointmentcaller.model.CallData;
import com.escribehost.appointmentcaller.phone.PhoneCaller;
import com.escribehost.shared.schedule.reminder.dto.AppointmentReminderCallDto;
import com.escribehost.shared.schedule.reminder.dto.AppointmentReminderStatus;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AppointmentReminderProcessorTest {
    private PhoneCaller phoneCaller;
    private AppointmentReminderPublisher appointmentReminderPublisher;
    private AppointmentReminderStatusPublisher appointmentReminderStatusPublisher;
    private AppointmentReminderProcessor appointmentReminderProcessor;

    @Before
    public void setUp() {
        phoneCaller = mock(PhoneCaller.class);
        appointmentReminderStatusPublisher = mock(AppointmentReminderStatusPublisher.class);
        appointmentReminderProcessor = new AppointmentReminderProcessor(phoneCaller, appointmentReminderStatusPublisher,
                appointmentReminderPublisher);
    }

    @Test
    public void test_processMessage() {
        // given
        AppointmentReminderCallDto appointmentReminderCallDto = new AppointmentReminderCallDto()
                .setStatus(AppointmentReminderStatus.NEW)
                .setAppointmentReminderId(1L)
                .setPhone("5183215324")
                .setAppointmentDate("2018-09-01")
                .setPatientFirstName("John")
                .setPatientLastName("Smith")
                .setProviderFirstName("Thomas")
                .setProviderLastName("Carter")
                .setLocation("New York");

        doAnswer((Answer<Void>) i -> {
            ((CallData) i.getArgument(0)).callEnd(AppointmentReminderStatus.COMPLETED, "123");
            return null;
        }).when(phoneCaller).call(any());

        // when
        appointmentReminderProcessor.processMessage(appointmentReminderCallDto);

        // then
        verify(phoneCaller).removeCall(any());
        verify(appointmentReminderStatusPublisher)
                .publish(appointmentReminderCallDto
                        .setStatus(AppointmentReminderStatus.COMPLETED)
                        .setCallId("123"));
    }
}
