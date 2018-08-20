package com.escribehost.appointmentcaller.job;

import com.escribehost.appointmentcaller.phone.CallData;
import com.escribehost.appointmentcaller.phone.PhoneCallerMock;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobManagerTest {
    private JobManagerImpl jobManager;

    @Before
    public void before() {
        jobManager = new JobManagerImpl(new PhoneCallerMock(), 10);
    }

    @Test
    public void getCallMessage() {
        Stream<CallData> callDataStream = IntStream
                .rangeClosed(1, 100)
                .mapToObj(i -> new CallData()
                        .setAppointmentId(String.valueOf(i))
                        .setPhoneToCall(String.valueOf(i))
                        .setAppointmentDate(DateTime.now().toDate())
                        .setHospitalName(String.valueOf(i))
                        .setPatientName(String.valueOf(i))
                        .setDoctor(String.valueOf(i)));

        List<Future<JobResult>> futures = callDataStream.map(cd -> jobManager.addJob(cd)).collect(Collectors.toList());
        List<JobResult> jobResults = futures.stream().map(f -> {
            try {
                return f.get();
            } catch (Exception e) {
                e.printStackTrace();
                return new JobResult(null, false);
            }
        }).collect(Collectors.toList());

        Assert.assertTrue(jobResults.stream().allMatch(j -> j.isSuccess()));
    }
}
