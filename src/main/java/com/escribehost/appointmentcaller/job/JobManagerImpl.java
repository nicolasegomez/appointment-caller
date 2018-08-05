package com.escribehost.appointmentcaller.job;

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
    public void addJob() {

    }

    //TODO: we need a worker to execute jobs
}
