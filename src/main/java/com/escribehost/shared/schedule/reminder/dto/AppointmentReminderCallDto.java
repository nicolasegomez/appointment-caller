package com.escribehost.shared.schedule.reminder.dto;

public class AppointmentReminderCallDto {
    private Long appointmentReminderId;
    private Long appointmentId;
    private AppointmentReminderStatus status;
    private String phone;
    private Long providerId;
    private Long roomId;
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
    private String callId;

    public Long getAppointmentReminderId() {
        return appointmentReminderId;
    }

    public AppointmentReminderCallDto setAppointmentReminderId(Long appointmentReminderId) {
        this.appointmentReminderId = appointmentReminderId;
        return this;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public AppointmentReminderCallDto setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
        return this;
    }

    public AppointmentReminderStatus getStatus() {
        return status;
    }

    public AppointmentReminderCallDto setStatus(AppointmentReminderStatus status) {
        this.status = status;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public AppointmentReminderCallDto setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Long getProviderId() {
        return providerId;
    }

    public AppointmentReminderCallDto setProviderId(Long providerId) {
        this.providerId = providerId;
        return this;
    }

    public Long getRoomId() {
        return roomId;
    }

    public AppointmentReminderCallDto setRoomId(Long roomId) {
        this.roomId = roomId;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public AppointmentReminderCallDto setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getRoom() {
        return room;
    }

    public AppointmentReminderCallDto setRoom(String room) {
        this.room = room;
        return this;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public AppointmentReminderCallDto setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
        return this;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public AppointmentReminderCallDto setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
        return this;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public AppointmentReminderCallDto setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
        return this;
    }

    public Integer getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public AppointmentReminderCallDto setAppointmentStartTime(Integer appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
        return this;
    }

    public String getProviderFirstName() {
        return providerFirstName;
    }

    public AppointmentReminderCallDto setProviderFirstName(String providerFirstName) {
        this.providerFirstName = providerFirstName;
        return this;
    }

    public String getProviderMiddleName() {
        return providerMiddleName;
    }

    public AppointmentReminderCallDto setProviderMiddleName(String providerMiddleName) {
        this.providerMiddleName = providerMiddleName;
        return this;
    }

    public String getProviderLastName() {
        return providerLastName;
    }

    public AppointmentReminderCallDto setProviderLastName(String providerLastName) {
        this.providerLastName = providerLastName;
        return this;
    }

    public AppointmentReminderType getType() {
        return type;
    }

    public AppointmentReminderCallDto setType(AppointmentReminderType type) {
        this.type = type;
        return this;
    }

    public String getCallId() {
        return callId;
    }

    public AppointmentReminderCallDto setCallId(String callId) {
        this.callId = callId;
        return this;
    }
}
