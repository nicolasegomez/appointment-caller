package com.escribehost.appointmentcaller.phone;

import java.util.Date;

public class CallData {
    private String appointmentId;
    private String phoneToCall;
    private String patientName;
    private String room;
    private String doctor;
    private String hospitalName;
    private String personToCall;
    private Date appointmentDate;

    public String getAppointmentId() {
        return appointmentId;
    }

    public CallData setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
        return this;
    }

    public String getPhoneToCall() {
        return phoneToCall;
    }

    public CallData setPhoneToCall(String phoneToCall) {
        this.phoneToCall = phoneToCall;
        return this;
    }

    public String getPatientName() {
        return patientName;
    }

    public CallData setPatientName(String patientName) {
        this.patientName = patientName;
        return this;
    }

    public String getRoom() {
        return room;
    }

    public CallData setRoom(String room) {
        this.room = room;
        return this;
    }

    public String getDoctor() {
        return doctor;
    }

    public CallData setDoctor(String doctor) {
        this.doctor = doctor;
        return this;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public CallData setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
        return this;
    }

    public String getPersonToCall() {
        return personToCall;
    }

    public CallData setPersonToCall(String personToCall) {
        this.personToCall = personToCall;
        return this;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public CallData setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
        return this;
    }
}
