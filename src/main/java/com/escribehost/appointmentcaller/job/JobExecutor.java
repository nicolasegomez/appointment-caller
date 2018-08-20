package com.escribehost.appointmentcaller.job;

import com.escribehost.appointmentcaller.phone.CallData;
import com.escribehost.appointmentcaller.phone.PhoneCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class JobExecutor implements Callable<JobResult> {
    private static final Logger logger = LoggerFactory.getLogger(JobExecutor.class);
    private JobManager jobManager;
    private PhoneCaller phoneCaller;

    public JobExecutor(JobManager jobManager, PhoneCaller phoneCaller) {
        this.phoneCaller = phoneCaller;
        this.jobManager = jobManager;
    }

    @Override
    public JobResult call() {
        CallData callData = jobManager.getNextCallData();
        if (callData != null) {
            try {
                phoneCaller.call(callData);
                callData.waitUntilCallEnd();
                phoneCaller.removeCall(callData);
                return handleEndedCall(callData);
            } catch (Exception ex) {
                logger.error("Call have failed. AppointmentId: {}, Error: {}", callData.getAppointmentId(), ex);
                return new JobResult(callData, false);
            }
        }
        return new JobResult(null, false);
    }

    public JobResult handleEndedCall(CallData callData) {
        if (!callData.isSuccesfullyCompleted()) {
            if (!callData.isMaxAttemptsReached()) {
                Future f = jobManager.addJob(callData);
                logger.warn("Call ended unsuccessfully. Scheduled for recall. AppointmentId: {}, Number of attempt done: {}",
                        callData.getAppointmentId(), callData.getAttempts());
                return new JobResult(callData, false, f);
            } else {
                logger.error("Call ended unsuccessfully. Max attempts reached. AppointmentId: {}", callData.getAppointmentId());
                return new JobResult(callData, false);
            }
        } else {
            logger.info("Call ended successfully, AppointmentId: {}", callData.getAppointmentId());
            return new JobResult(callData, true);
        }
    }
}
