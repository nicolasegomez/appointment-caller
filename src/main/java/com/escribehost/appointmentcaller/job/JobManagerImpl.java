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
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;

@Component
public class JobManagerImpl implements JobManager {

    private static final Logger logger = LoggerFactory.getLogger(JobManagerImpl.class);
    private int executorPoolSize;
    private PhoneCaller phoneCaller;
    private PriorityBlockingQueue<CallData> priorityCallDataQueue;
    private ExecutorService priorityJobPoolExecutor;

    @Autowired
    public JobManagerImpl(PhoneCaller phoneCaller, @Value("${executor.pool.size:15}") int executorPoolSize) {
        this.executorPoolSize = executorPoolSize;
        this.phoneCaller = phoneCaller;
        priorityCallDataQueue = new PriorityBlockingQueue<CallData>(11, Comparator.comparing(CallData::getPriority));
        priorityJobPoolExecutor = Executors.newFixedThreadPool(executorPoolSize);
    }

    @Override
    public Future<JobResult> addJob(CallData callData) {
        priorityCallDataQueue.put(callData);
        Future futureResponse = priorityJobPoolExecutor.submit(new JobExecutor(this, phoneCaller));
        logger.debug("Added job to queue. AppointmentId: {}. Priority: {}. Queue size: {}",
                callData.getAppointmentId(),
                callData.getPriority(),
                priorityCallDataQueue.size());
        return futureResponse;
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
