package com.escribehost.appointmentcaller.phone;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.TwiML;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Dial;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Redirect;
import com.twilio.twiml.voice.Say;
import com.twilio.type.PhoneNumber;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

@Component
public class PhoneCallerImpl implements PhoneCaller {

    private static final Logger logger = LoggerFactory.getLogger(PhoneCallerImpl.class);
    @Value("${twilio.account.sid}")
    public String twilioAccountSid;
    @Value("${twilio.auth.token}")
    public String twilioAuthToken;
    @Value("${twilio.phone.from}")
    public String twilioPhoneFrom;
    @Value("${phoneCallerApiUrl}")
    public String phoneCallerApiUrl;
    @Value("${supportPhone}")
    public String supportPhone;
    private HashMap<String, CallData> currentCalls;

    public PhoneCallerImpl() {
        currentCalls = new HashMap<String, CallData>();
    }

    private static void appendGather(VoiceResponse.Builder builder) {
        builder.gather(new Gather.Builder()
                .numDigits(1)
                .say(new Say.Builder("For confirm, press 1. For cancel or reschedule, press 2.").build())
                .build()
        )
                .redirect(new Redirect.Builder("/patient-response").build());
    }

    @Override
    public void call(CallData callData) throws URISyntaxException {
        Twilio.init(twilioAccountSid, twilioAuthToken);

        String to = callData.getPhoneToCall();

        Call call = Call
                .creator(new PhoneNumber(to), new PhoneNumber(twilioPhoneFrom), new URI(phoneCallerApiUrl + "/call"))
                .create();

        currentCalls.put(call.getSid(), callData);
        logger.info("Call started. SId:{}, AppiontmentId:{}", call.getSid(), callData.getAppointmentId());
    }

    public String getCallMessage(CallData call, String templateFileName) {
        String appointmentInfo = "";
        if (call.getDoctor() != null)
            appointmentInfo += " with the doctor " + call.getDoctor();
        if (call.getRoom() != null)
            appointmentInfo += " in the room " + call.getRoom();

        JtwigTemplate template = JtwigTemplate.classpathTemplate(templateFileName);
        JtwigModel model = JtwigModel.newModel()
                .with("patientName", call.getPatientName())
                .with("appointmentDate", call.getAppointmentDate())
                .with("appointmentInfo", appointmentInfo)
                .with("hospitalName", call.getHospitalName());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        template.render(model, stream);
        return stream.toString();
    }

    @Override
    public TwiML getWelcomeDialog(String callSid) {
        CallData call = currentCalls.get(callSid);
        TwiML twiml;
        if (call != null) {
            twiml = new VoiceResponse.Builder()
                    .gather(
                            new Gather.Builder()
                                    .numDigits(1)
                                    .action("/call/patient-response")
                                    .say(new Say.Builder(getCallMessage(call, "templates/welcome.twig")).build())
                                    .build()
                    )

                    .build();
            logger.debug("Welcome dialog send for SId:{}", callSid);
        } else {

            twiml = new VoiceResponse.Builder()
                    .say(new Say.Builder(getCallMessage(call, "templates/problem.twig")).build())
                    .build();
            logger.error("Welcome dialog can't be sent because the Call doesn't exists. SId:{}", callSid);
        }
        return twiml;
    }

    @Override
    public TwiML handleResponse(String callSid, String digits) {
        // Create a TwiML response and add our friendly message.
        CallData call = currentCalls.get(callSid);
        if (call != null) {
            VoiceResponse.Builder builder = new VoiceResponse.Builder();
            if (digits != null) {
                switch (digits) {
                    case "1":
                        builder.say(new Say.Builder(getCallMessage(call, "templates/confirm.twig")).build());
                        currentCalls.remove(callSid);
                        logger.info("Patient confirmed appointment. SId:{}, AppointmentId:{}", callSid, call.getAppointmentId());
                        break;
                    case "2":
                        builder.say(new Say.Builder(getCallMessage(call, "templates/cancel.twig")).build());
                        builder.dial(new Dial.Builder(supportPhone).build());
                        currentCalls.remove(callSid);
                        logger.info("Patient requested for support in appointment. SId:{}, AppointmentId:{}", callSid, call.getAppointmentId());
                        break;
                    default:
                        builder.say(new Say.Builder(getCallMessage(call, "templates/nochoice.twig")).build());
                        appendGather(builder);
                        logger.info("Patient selected wrong response for appointment. SId:{}, AppointmentId:{}", callSid, call.getAppointmentId());
                        break;
                }

            } else {
                appendGather(builder);
                logger.info("Patient haven't selected a response for appointment. SId:{}, AppointmentId:{}", callSid, call.getAppointmentId());
            }

            return builder.build();
        } else {
            logger.error("Options dialog can't be sent because the Call doesn't exists. SId:{}", callSid);
            return new VoiceResponse.Builder()
                    .say(new Say.Builder(getCallMessage(call, "templates/problem.twig")).build())
                    .build();

        }
    }

}
