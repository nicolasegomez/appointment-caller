package com.escribehost.appointmentcaller.job;

import com.escribehost.appointmentcaller.phone.CallData;
import com.escribehost.appointmentcaller.phone.PhoneCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

@Component
public class JobManagerImpl implements JobManager {

    private static final Logger logger = LoggerFactory.getLogger(JobManagerImpl.class);
    @Value("${executor.pool.size}")
    public int executorPoolSize;
    private PhoneCaller phoneCaller;
    private PriorityBlockingQueue<CallData> priorityCallDataQueue;
    private ExecutorService priorityJobPoolExecutor;

    @Autowired
    public JobManagerImpl(PhoneCaller phoneCaller) {
        this.phoneCaller = phoneCaller;
        priorityCallDataQueue = new PriorityBlockingQueue<CallData>(11, Comparator.comparing(CallData::getPriority));
        priorityJobPoolExecutor = Executors.newFixedThreadPool(executorPoolSize);
    }

    @Override
    public void addJob(CallData callData) {
        priorityCallDataQueue.put(callData);
        priorityJobPoolExecutor.execute(new JobExecutor(this, phoneCaller));
        logger.debug("Added job to queue. AppointmentId: {}. Priority: {}. Queue size: {}",
                callData.getAppointmentId(),
                callData.getPriority(),
                priorityCallDataQueue.size());
    }

    @Override
    public CallData getNextCallData() {
        CallData callData = priorityCallDataQueue.poll();
        logger.debug("Get job from queue. AppointmentId: {}. Priority: {}. Queue size: {}",
                callData.getAppointmentId(),
                callData.getPriority(),
                priorityCallDataQueue.size());
        return callData;
    }
}
