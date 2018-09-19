package com.escribehost.appointmentcaller.phone;

import com.escribehost.appointmentcaller.model.CallData;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PhoneCallerTest {
    @Autowired
    private PhoneCaller phoneCaller;

    @Test
    @Ignore
    public void callTest() throws URISyntaxException {
        phoneCaller.call(new CallData()
                .setCallId("1")
                .setPhoneToCall("+5491130687450")
                .setAppointmentDate(new Date())
                .setLocationName("Capital Cardiology Hospital")
                .setPatientName("Nicolas Gomez")
                .setProvider("Robert James"));
    }

    @Test
    public void getCallMessage() {
        String message = phoneCaller.getCallMessage(
                new CallData()
                        .setCallId("1")
                        .setPhoneToCall("+5491130687450")
                        .setAppointmentDate(DateTime.now().toDate())
                        .setLocationName("Capital Cardiology Hospital")
                        .setPatientName("Nicolas Gomez")
                        .setProvider("Robert James"),
                "templates/reminder-welcome.twig");
        System.out.println(message);
        Assert.assertNotEquals("", message);
    }
}
