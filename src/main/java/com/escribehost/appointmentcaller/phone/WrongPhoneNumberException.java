package com.escribehost.appointmentcaller.phone;

public class WrongPhoneNumberException extends RuntimeException {
    public WrongPhoneNumberException(String message) {
        super(message);
    }
}
