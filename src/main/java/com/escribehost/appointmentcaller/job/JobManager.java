package com.escribehost.appointmentcaller.job;

import com.escribehost.appointmentcaller.phone.CallData;

import java.util.concurrent.Future;

public interface JobManager {
    Future<JobResult> addJob(CallData callData);

    CallData getNextCallData();
}
