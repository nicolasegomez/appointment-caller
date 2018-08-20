package com.escribehost.appointmentcaller.phone;

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
                .setPhoneToCall("+5491130687450")
                .setAppointmentDate(new Date())
                .setHospitalName("Capital Cardiology Hospital")
                .setPatientName("Nicolas Gomez")
                .setDoctor("Robert James"));
    }

    @Test
    public void getCallMessage() {
        String message = phoneCaller.getCallMessage(
                new CallData()
                        .setPhoneToCall("+5491130687450")
                        .setAppointmentDate(DateTime.now().toDate())
                        .setHospitalName("Capital Cardiology Hospital")
                        .setPatientName("Nicolas Gomez")
                        .setDoctor("Robert James"),
                "templates/welcome.twig");
        System.out.println(message);
        Assert.assertNotEquals("", message);
    }
}
