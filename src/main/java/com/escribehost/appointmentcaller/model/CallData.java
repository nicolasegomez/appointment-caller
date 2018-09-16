package com.escribehost.appointmentcaller.model;

import com.twilio.rest.api.v2010.account.Call;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

public class CallData implements Serializable {
    @Value("${call.timeout:10000}")
    private int timeoutCallData;

    private Long appointmentId;
    private String phoneToCall;
    private String patientName;
    private String room;
    private String provider;
    private String locationName;
    private Date appointmentDate;
    private Integer appointmentStartTime;
    private AppointmentReminderType type;

    private boolean callFinished = false;

    private Call.Status callEndStatus;
    private Integer userResponse = null;

    public Long getAppointmentId() {
        return appointmentId;
    }

    public CallData setAppointmentId(Long appointmentId) {
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

    public String getProvider() {
        return provider;
    }

    public CallData setProvider(String provider) {
        this.provider = provider;
        return this;
    }

    public String getLocationName() {
        return locationName;
    }

    public CallData setLocationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public CallData setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
        return this;
    }

    public Optional<Call.Status> getCallEndStatus() {
        return Optional.ofNullable(callEndStatus);
    }

    public CallData setUserResponse(Integer userResponse) {
        this.userResponse = userResponse;
        return this;
    }

    public Integer getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public CallData setAppointmentStartTime(Integer appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
        return this;
    }

    public AppointmentReminderType getType() {
        return type;
    }

    public CallData setType(AppointmentReminderType type) {
        this.type = type;
        return this;
    }

    public synchronized void waitUntilCallEnd() throws Exception {
        if (!callFinished) {
            wait(timeoutCallData);
            if (!callFinished) {
                throw new Exception("The call wrong finished");
            }
        }
    }

    public synchronized void callEnd(Call.Status callStatus) {

        this.callEndStatus = callStatus;
        callFinished = true;
        notify();
    }

    public boolean isSuccesfullyCompleted() {
        return callFinished
                && userResponse != null
                && this.callEndStatus.equals(Call.Status.COMPLETED);
    }

}
