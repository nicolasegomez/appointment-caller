package com.escribehost.appointmentcaller.job;

import com.escribehost.appointmentcaller.phone.CallData;
import com.escribehost.appointmentcaller.phone.PhoneCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobExecutor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(JobExecutor.class);
    private JobManager jobManager;
    private PhoneCaller phoneCaller;

    public JobExecutor(JobManager jobManager, PhoneCaller phoneCaller) {
        this.phoneCaller = phoneCaller;
        this.jobManager = jobManager;
    }

    @Override
    public void run() {

        CallData callData = jobManager.getNextCallData();
        if (callData != null) {
            try {
                phoneCaller.call(callData);
            } catch (Exception ex) {
                logger.error("Call have failed. AppointmentId: {}, Error: {}", callData.getAppointmentId(), ex);
            }
        }
    }
}
