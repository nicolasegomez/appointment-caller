package com.escribehost.appointmentcaller.phone;

import org.junit.Assert;
import org.junit.Test;

public class PhoneNumberParserTest {
    @Test
    public void testPhoneWithExtX4Successful() {
        PhoneNumberParser phone1 = PhoneNumberParser.parse("6705239488x5089");
        Assert.assertEquals("6705239488", phone1.getPhone());
        Assert.assertTrue(phone1.hasExtension());
        Assert.assertEquals("5089", phone1.getExtension());
    }

    public void testPhoneWithExtX3Successful() {
        PhoneNumberParser phone1 = PhoneNumberParser.parse("6705239488x508");
        Assert.assertEquals("6705239488", phone1.getPhone());
        Assert.assertTrue(phone1.hasExtension());
        Assert.assertEquals("508", phone1.getExtension());
    }

    public void testPhoneWithExtX2Successful() {
        PhoneNumberParser phone1 = PhoneNumberParser.parse("6705239488x50");
        Assert.assertEquals("6705239488", phone1.getPhone());
        Assert.assertTrue(phone1.hasExtension());
        Assert.assertEquals("50", phone1.getExtension());
    }

    public void testPhoneWithExtX1Successful() {
        PhoneNumberParser phone1 = PhoneNumberParser.parse("6705239488x5");
        Assert.assertEquals("6705239488", phone1.getPhone());
        Assert.assertTrue(phone1.hasExtension());
        Assert.assertEquals("5", phone1.getExtension());
    }

    @Test
    public void testPhoneWithoutExtSuccessful() {
        PhoneNumberParser phone1 = PhoneNumberParser.parse("6705239488");
        Assert.assertEquals("6705239488", phone1.getPhone());
        Assert.assertFalse(phone1.hasExtension());
    }

    @Test
    public void testPhoneWithExtHashSuccessful() {
        PhoneNumberParser phone1 = PhoneNumberParser.parse("6705239488#508");
        Assert.assertEquals("6705239488", phone1.getPhone());
        Assert.assertTrue(phone1.hasExtension());
        Assert.assertEquals("508", phone1.getExtension());
    }

    @Test(expected = RuntimeException.class)
    public void testPhoneWithoutExtButXUnsuccessful() {
        PhoneNumberParser.parse("6705239488x");
    }

    @Test(expected = RuntimeException.class)
    public void testLargePhoneUnsuccessful() {
        PhoneNumberParser.parse("6705239488899x66");
    }

    @Test(expected = RuntimeException.class)
    public void testShortPhoneUnsuccessful() {
        PhoneNumberParser.parse("670528x45");
    }

    @Test(expected = RuntimeException.class)
    public void testWrongPhoneUnsuccessful() {
        PhoneNumberParser.parse("670-528-2143x4512");
    }

    @Test(expected = RuntimeException.class)
    public void testWrongExtensionPhoneUnsuccessful() {
        PhoneNumberParser.parse("6705282143x4A51");
    }
}
