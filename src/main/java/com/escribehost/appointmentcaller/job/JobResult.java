package com.escribehost.appointmentcaller.job;

import com.escribehost.appointmentcaller.phone.CallData;

import java.util.concurrent.Future;

public class JobResult {

    private CallData callData;
    private boolean success;
    private Future<JobResult> nextAttempt;

    public JobResult(CallData callData, boolean success) {
        this(callData, success, null);
    }

    public JobResult(CallData callData, boolean success, Future<JobResult> nextAttempt) {
        this.callData = callData;
        this.success = success;
        this.nextAttempt = nextAttempt;
    }

    public CallData getCallData() {
        return callData;
    }

    public JobResult setCallData(CallData callData) {
        this.callData = callData;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public JobResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public Future<JobResult> getNextAttempt() {
        return nextAttempt;
    }

    public JobResult setNextAttempt(Future<JobResult> nextAttempt) {
        this.nextAttempt = nextAttempt;
        return this;
    }
}
