package com.escribehost.appointmentcaller.phone.twilio;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.escribehost.appointmentcaller.model.CallData;
import com.escribehost.appointmentcaller.phone.PhoneCaller;
import com.escribehost.appointmentcaller.phone.PhoneNumberParser;
import com.escribehost.shared.schedule.reminder.dto.AppointmentReminderStatus;
import com.escribehost.shared.schedule.reminder.dto.AppointmentReminderType;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.CallCreator;
import com.twilio.twiml.TwiML;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Dial;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Redirect;
import com.twilio.twiml.voice.Say;
import com.twilio.type.PhoneNumber;

@Component
@Profile("!dev")
public class PhoneCallerTwilio implements PhoneCaller {

    private static final Logger logger = LoggerFactory.getLogger(PhoneCallerTwilio.class);
    private static final List<Call.Status> endStatuses = Arrays.asList(Call.Status.BUSY,
            Call.Status.COMPLETED, Call.Status.CANCELED, Call.Status.FAILED, Call.Status.NO_ANSWER);

    @Value("${phoneCallerApiUrl}")
    public String phoneCallerApiUrl;
    @Value("${supportPhone}")
    public String supportPhone;
    protected HashMap<String, CallData> currentCalls;
    private TwilioConfig twilioConfig;

    public PhoneCallerTwilio(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
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
    public void call(CallData callData) {
        try {
            Twilio.init(twilioConfig.getAccountSid(callData.getAccountId()),
                    twilioConfig.getToken(callData.getAccountId()));

            PhoneNumberParser phoneNumberTo = PhoneNumberParser.parse(callData.getPhoneToCall());
            String to = phoneNumberTo.getPhone();

            String callerApiUrlPath = "/call";
            if (callData.getType() == AppointmentReminderType.CONFIRMATION) {
                callerApiUrlPath += "/reminder";
            } else {
                callerApiUrlPath += "/cancellation";
            }

            CallCreator callCreator = Call
                    .creator(new PhoneNumber(to), new PhoneNumber(twilioConfig.getPhoneFrom(callData.getAccountId())),
                            new URI(phoneCallerApiUrl + callerApiUrlPath))
                    .setStatusCallback(new URI(phoneCallerApiUrl + "call/status"))
                    .setRecord(true);

            if (phoneNumberTo.hasExtension()) {
                callCreator.setSendDigits(phoneNumberTo.getExtension());
            }

            Call call = callCreator.create();

            currentCalls.put(call.getSid(), callData);
            logger.info("Call started. SId:{}, AppointmentId:{}", call.getSid(), callData.getAppointmentId());
        } catch (Exception ex) {
            logger.error("Call failed! AppointmentId: " + callData.getAppointmentId(), ex);
            callData.callEnd(AppointmentReminderStatus.FAILED, null);
        }
    }

    protected String getCallMessage(CallData call, String templateFileName) {
        String appointmentInfo = "";
        if (call.getProvider() != null) {
            appointmentInfo += " with the doctor " + call.getProvider();
        }
        if (call.getRoom() != null) {
            appointmentInfo += " in the room " + call.getRoom();
        }

        JtwigTemplate template = JtwigTemplate.classpathTemplate(templateFileName);
        JtwigModel model = JtwigModel.newModel()
                .with("patientName", call.getPatientName())
                .with("appointmentDate", call.getAppointmentDate())
                .with("appointmentInfo", appointmentInfo)
                .with("hospitalName", call.getLocationName());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        template.render(model, stream);
        return stream.toString();
    }

    @Override
    public void removeCall(CallData callData) {
        currentCalls.remove(callData);
    }

    @Override
    public TwiML getReminderWelcomeDialog(String callSid) {
        CallData call = currentCalls.get(callSid);
        TwiML twiml;
        if (call != null) {
            twiml = new VoiceResponse.Builder()
                    .gather(
                            new Gather.Builder()
                                    .numDigits(1)
                                    .action("/call/reminder/patient-response")
                                    .say(new Say.Builder(getCallMessage(call, "templates/reminder-welcome.twig"))
                                            .build())
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
    public TwiML getCancellationWelcomeDialog(String callSid) {
        CallData call = currentCalls.get(callSid);
        TwiML twiml;
        if (call != null) {
            twiml = new VoiceResponse.Builder()
                    .gather(
                            new Gather.Builder()
                                    .numDigits(1)
                                    .action("/call/cancellation/patient-response")
                                    .say(new Say.Builder(getCallMessage(call, "templates/cancellation-welcome.twig"))
                                            .build())
                                    .build()
                    )

                    .build();
            logger.debug("Cancellation dialog send for SId:{}", callSid);
        } else {

            twiml = new VoiceResponse.Builder()
                    .say(new Say.Builder(getCallMessage(call, "templates/problem.twig")).build())
                    .build();
            logger.error("Cancellation dialog can't be sent because the Call doesn't exists. SId:{}", callSid);
        }
        return twiml;
    }

    @Override
    public TwiML handleReminderResponse(String callSid, String digits) {
        // Create a TwiML response and add our friendly message.
        CallData call = currentCalls.get(callSid);
        if (call != null) {
            VoiceResponse.Builder builder = new VoiceResponse.Builder();
            if (digits != null) {
                switch (digits) {
                    case "1":
                        call.setUserResponse(Integer.parseInt(digits));
                        builder.say(new Say.Builder(getCallMessage(call, "templates/reminder-confirm.twig")).build());
                        logger.info("Patient confirmed appointment. SId:{}, AppointmentId:{}", callSid,
                                call.getAppointmentId());
                        break;
                    case "2":
                        call.setUserResponse(Integer.parseInt(digits));
                        builder.say(new Say.Builder(getCallMessage(call, "templates/cancel.twig")).build());
                        builder.dial(new Dial.Builder(supportPhone).build());
                        logger.info("Patient requested for support in appointment. SId:{}, AppointmentId:{}", callSid,
                                call.getAppointmentId());
                        break;
                    default:
                        builder.say(new Say.Builder(getCallMessage(call, "templates/nochoice.twig")).build());
                        appendGather(builder);
                        logger.info("Patient selected wrong response for appointment. SId:{}, AppointmentId:{}",
                                callSid, call.getAppointmentId());
                        break;
                }

            } else {
                appendGather(builder);
                logger.info("Patient haven't selected a response for appointment. SId:{}, AppointmentId:{}", callSid,
                        call.getAppointmentId());
            }

            return builder.build();
        } else {

            logger.error("Options dialog can't be sent because the Call doesn't exists. SId:{}", callSid);
            return new VoiceResponse.Builder()
                    .say(new Say.Builder(getCallMessage(call, "templates/problem.twig")).build())
                    .build();
        }
    }

    @Override
    public TwiML handleCancellationResponse(String callSid, String digits) {
        // Create a TwiML response and add our friendly message.
        CallData call = currentCalls.get(callSid);
        if (call != null) {
            VoiceResponse.Builder builder = new VoiceResponse.Builder();
            if (digits != null) {
                switch (digits) {
                    case "1":
                        call.setUserResponse(Integer.parseInt(digits));
                        builder.say(new Say.Builder(getCallMessage(call, "templates/cancellation-reschedule.twig"))
                                .build());
                        builder.dial(new Dial.Builder(supportPhone).build());
                        currentCalls.remove(callSid);
                        logger.info(
                                "Patient decided to reschedule the appointment because this was a cancellation. " +
                                        "SId:{}, AppointmentId:{}",
                                callSid, call.getAppointmentId());
                        break;
                    default:
                        builder.say(new Say.Builder(getCallMessage(call, "templates/nochoice.twig")).build());
                        appendGather(builder);
                        logger.info("Patient selected wrong response for appointment. SId:{}, AppointmentId:{}",
                                callSid, call.getAppointmentId());
                        break;
                }

            } else {
                appendGather(builder);
                logger.info("Patient haven't selected a response for appointment. SId:{}, AppointmentId:{}", callSid,
                        call.getAppointmentId());
            }

            return builder.build();
        } else {

            logger.error("Options dialog can't be sent because the Call doesn't exists. SId:{}", callSid);
            return new VoiceResponse.Builder()
                    .say(new Say.Builder(getCallMessage(call, "templates/problem.twig")).build())
                    .build();
        }
    }

    @Override
    public void handleStatus(String callSid, Call.Status callStatus, String called) {
        logger.info("Call status SId: {}, status: {}, calledTo:{}", callSid, callStatus, called);

        if (endStatuses.contains(callStatus)) {
            CallData call = currentCalls.get(callSid);
            if (call != null) {
                call.callEnd(getAppointmentReminderStatus(callStatus), callSid);
                currentCalls.remove(callSid);
            } else {

                logger.error("Can't handle call status because the Call doesn't exists. SId:{}", callSid);
            }
        }
    }

    private AppointmentReminderStatus getAppointmentReminderStatus(Call.Status callStatus) {
        switch (callStatus) {
            case COMPLETED:
                return AppointmentReminderStatus.COMPLETED;
            case FAILED:
                return AppointmentReminderStatus.FAILED;
            case BUSY:
                return AppointmentReminderStatus.BUSY;
            case CANCELED:
                return AppointmentReminderStatus.CANCELED;
            case NO_ANSWER:
                return AppointmentReminderStatus.NO_ANSWER;
            default:
                return AppointmentReminderStatus.FAILED;
        }
    }
}
