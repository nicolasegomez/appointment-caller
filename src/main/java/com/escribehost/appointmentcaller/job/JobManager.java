package com.escribehost.appointmentcaller.job;

import com.escribehost.appointmentcaller.phone.CallData;

public interface JobManager {
    void addJob(CallData callData);

    CallData getNextCallData();
}
