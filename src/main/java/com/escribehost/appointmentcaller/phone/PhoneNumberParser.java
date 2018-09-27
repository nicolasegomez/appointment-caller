package com.escribehost.appointmentcaller.phone;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class PhoneNumberParser {

    private static String PN_REGEX = "^(?<PHONE>[2-9]\\d{2}[2-9]\\d{2}\\d{4})([x|#](?<EXT>\\d+))?$";
    private static Pattern PN_PATTERN = Pattern.compile(PN_REGEX, Pattern.CASE_INSENSITIVE);
    private String phone;
    private String extension;

    public PhoneNumberParser(String phone, String extension) {
        if (StringUtils.isEmpty(phone)) {
            throw new RuntimeException("Invalid phone number");
        }
        this.phone = phone;
        this.extension = extension;
    }

    public static PhoneNumberParser parse(String phoneNumber) {
        Matcher matcher = PN_PATTERN.matcher(phoneNumber);
        if (!matcher.matches()) {
            throw new WrongPhoneNumberException("Invalid phone number: " + phoneNumber);
        }
        return new PhoneNumberParser(matcher.group("PHONE"), matcher.group("EXT"));
    }

    public String getPhone() {
        return phone;
    }

    public String getExtension() {
        return extension;
    }

    public boolean hasExtension() {
        return !StringUtils.isEmpty(this.extension);
    }

}
