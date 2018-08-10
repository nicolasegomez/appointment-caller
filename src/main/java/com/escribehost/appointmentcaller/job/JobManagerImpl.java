package com.escribehost.appointmentcaller.job;

import com.escribehost.appointmentcaller.phone.CallData;
import com.escribehost.appointmentcaller.phone.PhoneCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobManagerImpl implements JobManager {
    private PhoneCaller phoneCaller;

    @Autowired
    public JobManagerImpl(PhoneCaller phoneCaller) {
        this.phoneCaller = phoneCaller;
    }

    @Override
    public void addJob(CallData callData) {
        try {
            phoneCaller.call(callData);
        } catch (Exception ex) {
            //TODO: ver esto
        }
    }


    //TODO: we need a worker to execute jobs
}
