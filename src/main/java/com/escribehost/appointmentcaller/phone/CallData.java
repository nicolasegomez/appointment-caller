package com.escribehost.appointmentcaller.phone;

import com.twilio.rest.api.v2010.account.Call;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

public class CallData implements Serializable{
    @Value("${call.timeout}")
    private int timeoutCallData;
    @Value("${call.maxAttempts}")
    private int maxAttempts;

    private String appointmentId;
    private String phoneToCall;
    private String patientName;
    private String room;
    private String doctor;
    private String hospitalName;
    private String personToCall;
    private Date appointmentDate;
    private int priority;
    private int attempts = 0;
    private boolean callFinished = false;

    private Call.Status callEndStatus;
    private Integer userResponse = null;

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

    public int getPriority() {
        return priority;
    }

    public CallData setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public Optional<Call.Status> getCallEndStatus() {
        return Optional.ofNullable(callEndStatus);
    }

    public CallData setUserResponse(Integer userResponse) {
        this.userResponse = userResponse;
        return this;
    }

    public int getAttempts() {
        return attempts;
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
        this.attempts ++;
        this.callEndStatus = callStatus;
        callFinished = true;
        notify();
    }

    public boolean isSuccesfullyCompleted() {
        return callFinished
                && userResponse != null
                && this.callEndStatus.equals(Call.Status.COMPLETED);
    }

    public boolean isMaxAttemptsReached() {
        return attempts >= maxAttempts;
    }
}
