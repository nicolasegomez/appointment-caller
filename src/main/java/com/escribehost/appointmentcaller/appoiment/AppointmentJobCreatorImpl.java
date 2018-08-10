package com.escribehost.appointmentcaller.appoiment;

import com.escribehost.appointmentcaller.job.JobManager;
import com.escribehost.appointmentcaller.phone.CallData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AppointmentJobCreatorImpl implements AppointmentJobCreator {
    private JobManager jobManager;

    @Autowired
    public AppointmentJobCreatorImpl(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @Override
    public void createConfirmationCallJobs() {
        jobManager.addJob(new CallData().setPhoneToCall("+5491130687450").setAppointmentDate(new Date()).setHospitalName("Capital Cardiology Hospital").setPatientName("Nicolas Gomez").setDoctor("Robert James"));
    }

    @Override
    public void createRescheduleCallJobs() {

    }
}
