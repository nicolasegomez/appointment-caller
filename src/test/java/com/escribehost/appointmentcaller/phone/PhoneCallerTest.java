package com.escribehost.appointmentcaller.phone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PhoneCallerTest {
    @Autowired
    private PhoneCaller phoneCaller;

    @Test
    public void callTest() throws URISyntaxException {
        phoneCaller.call();
    }
}
