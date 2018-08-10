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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

@Component
public class PhoneCallerImpl implements PhoneCaller {

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
    }

    public String getCallMessage(CallData call) {
        String appointmentInfo = "";
        if (call.getDoctor() != null)
            appointmentInfo += " with the doctor " + call.getDoctor();
        if (call.getRoom() != null)
            appointmentInfo += " in the room " + call.getRoom();
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/welcome.twig");
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
                                    .say(new Say.Builder(getCallMessage(call)).build())
                                    .build()
                    )

                    .build();

        } else {
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/problem.twig");
            JtwigModel model = JtwigModel.newModel();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            template.render(model, stream);

            twiml = new VoiceResponse.Builder()
                    .say(new Say.Builder(stream.toString()).build())
                    .build();
        }
        return twiml;
    }

    @Override
    public TwiML handleResponse(String callSid, String digits) {
        // Create a TwiML response and add our friendly message.
        CallData call = currentCalls.get(callSid);
        if(call!=null) {
            VoiceResponse.Builder builder = new VoiceResponse.Builder();
            JtwigTemplate template;
            JtwigModel model;
            ByteArrayOutputStream stream;
            if (digits != null) {
                switch (digits) {
                    case "1":
                        template = JtwigTemplate.classpathTemplate("templates/confirm.twig");
                        model = JtwigModel.newModel();
                        stream = new ByteArrayOutputStream();
                        template.render(model, stream);
                        builder.say(new Say.Builder(stream.toString()).build());
                        currentCalls.remove(callSid);
                        break;
                    case "2":
                        template = JtwigTemplate.classpathTemplate("templates/cancel.twig");
                        model = JtwigModel.newModel();
                        stream = new ByteArrayOutputStream();
                        template.render(model, stream);
                        builder.say(new Say.Builder(stream.toString()).build());
                        builder.dial(new Dial.Builder(supportPhone).build());
                        currentCalls.remove(callSid);
                        break;
                    default:
                        template = JtwigTemplate.classpathTemplate("templates/nochoice.twig");
                        model = JtwigModel.newModel();
                        stream = new ByteArrayOutputStream();
                        template.render(model, stream);
                        builder.say(new Say.Builder(stream.toString()).build());
                        appendGather(builder);
                        break;
                }

            } else {
                appendGather(builder);
            }

            return builder.build();
        }
        else {
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/problem.twig");
            JtwigModel model = JtwigModel.newModel();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            template.render(model, stream);

            return new VoiceResponse.Builder()
                    .say(new Say.Builder(stream.toString()).build())
                    .build();
        }
    }

}
