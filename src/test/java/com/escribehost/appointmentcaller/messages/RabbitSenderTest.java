package com.escribehost.appointmentcaller.messages;

import com.escribehost.appointmentcaller.phone.CallData;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@Import(MessageTestConfiguration.class)
@RunWith(SpringRunner.class)
public class RabbitSenderTest {
    @Autowired
    private RabbitTemplate template;

    @Test
    public void sendMessageTest() {
        CallData message = new CallData()
                .setPhoneToCall("+5491130687450")
                .setAppointmentDate(DateTime.now().toDate())
                .setHospitalName("Capital Cardiology Hospital")
                .setPatientName("Nicolas Gomez")
                .setDoctor("Robert James");
        template.convertAndSend(message);
    }
}
