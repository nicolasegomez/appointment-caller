package com.escribehost.appointmentcaller.appoiment;

import com.escribehost.appointmentcaller.job.JobManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentJobCreatorImpl implements AppointmentJobCreator {
    private JobManager jobManager;

    @Autowired
    public AppointmentJobCreatorImpl(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @Override
    public void createConfirmCallJobs() {

    }

    @Override
    public void createRescheduleCallJobs() {

    }
}
