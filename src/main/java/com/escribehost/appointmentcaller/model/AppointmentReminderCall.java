package com.escribehost.appointmentcaller.model;

public class AppointmentReminderCall {
    private Long appointmentReminderId;
    private Long appointmentId;
    private AppointmentReminderStatus status;
    private String phone;
    private String location;
    private String room;
    private String patientFirstName;
    private String patientLastName;
    private String appointmentDate;
    private Integer appointmentStartTime;
    private String providerFirstName;
    private String providerMiddleName;
    private String providerLastName;
    private AppointmentReminderType type;

    public Long getAppointmentReminderId() {
        return appointmentReminderId;
    }

    public AppointmentReminderCall setAppointmentReminderId(Long appointmentReminderId) {
        this.appointmentReminderId = appointmentReminderId;
        return this;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public AppointmentReminderCall setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
        return this;
    }

    public AppointmentReminderStatus getStatus() {
        return status;
    }

    public AppointmentReminderCall setStatus(AppointmentReminderStatus status) {
        this.status = status;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public AppointmentReminderCall setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public AppointmentReminderCall setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getRoom() {
        return room;
    }

    public AppointmentReminderCall setRoom(String room) {
        this.room = room;
        return this;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public AppointmentReminderCall setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
        return this;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public AppointmentReminderCall setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
        return this;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public AppointmentReminderCall setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
        return this;
    }

    public Integer getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public AppointmentReminderCall setAppointmentStartTime(Integer appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
        return this;
    }

    public String getProviderFirstName() {
        return providerFirstName;
    }

    public AppointmentReminderCall setProviderFirstName(String providerFirstName) {
        this.providerFirstName = providerFirstName;
        return this;
    }

    public String getProviderMiddleName() {
        return providerMiddleName;
    }

    public AppointmentReminderCall setProviderMiddleName(String providerMiddleName) {
        this.providerMiddleName = providerMiddleName;
        return this;
    }

    public String getProviderLastName() {
        return providerLastName;
    }

    public AppointmentReminderCall setProviderLastName(String providerLastName) {
        this.providerLastName = providerLastName;
        return this;
    }

    public AppointmentReminderType getType() {
        return type;
    }

    public AppointmentReminderCall setType(AppointmentReminderType type) {
        this.type = type;
        return this;
    }
}
