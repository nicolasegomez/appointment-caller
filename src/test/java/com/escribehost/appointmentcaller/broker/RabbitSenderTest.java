package com.escribehost.appointmentcaller.broker;

import com.escribehost.appointmentcaller.model.CallData;
import org.joda.time.DateTime;
import org.junit.Ignore;
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
    @Ignore
    public void sendMessageTest() {
        for (int i=0;i<100;i++) {
            CallData message = new CallData()
                    .setPhoneToCall("+5491130687450")
                    .setAppointmentDate(DateTime.now().toDate())
                    .setLocationName("Capital Cardiology Hospital")
                    .setPatientName("Nicolas Gomez")
                    .setProvider("Robert James")
                    .setAppointmentId(new Long(i));

            template.convertAndSend(message);
        }
    }
}
