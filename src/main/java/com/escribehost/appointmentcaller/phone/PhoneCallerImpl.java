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
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class PhoneCallerImpl implements PhoneCaller {

    public static final String ACCOUNT_SID = "AC28de80ab54ca5e6bd85105400bc6f430";
    public static final String AUTH_TOKEN = "add5d01384a6862c1ae6f5b7d7f98b51";

    private static void appendGather(VoiceResponse.Builder builder) {
        builder.gather(new Gather.Builder()
                .numDigits(1)
                .say(new Say.Builder("For confirm, press 1. For cancel or reschedule, press 2.").build())
                .build()
        )
                .redirect(new Redirect.Builder("/patient-response").build());
    }

    @Override
    public void call() throws URISyntaxException {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String from = "+12564747896";
        String to = "+5491130687450";

        Call call = Call
                .creator(new PhoneNumber(to), new PhoneNumber(from), new URI("http://190.192.169.3:8080/call"))
                .create();

        System.out.println(call.getSid());
    }

    @Override
    public TwiML getWelcomeDialog() {
        // Create a TwiML response and add our friendly message.
        TwiML twiml = new VoiceResponse.Builder()
                .gather(
                        new Gather.Builder()
                                .numDigits(1)
                                .action("/call/patient-response")
                                .say(new Say.Builder("Hi, you have an appointment for tomorrow with doctor Nico. For confirm, press 1. For cancel or reschedule, press 2.").build())
                                .build()
                )
                //.redirect(new Redirect.Builder("/call/patient-response").build())
                .build();
        return twiml;
    }

    @Override
    public TwiML handleResponse(String digits) {
        // Create a TwiML response and add our friendly message.
        VoiceResponse.Builder builder = new VoiceResponse.Builder();

        if (digits != null) {
            switch (digits) {
                case "1":
                    builder.say(new Say.Builder("You confirm the appointment. Good for you!").build());
                    break;
                case "2":
                    builder.say(new Say.Builder("You need support. We will help!").build());
                    builder.dial(new Dial.Builder("+5491155632686").build());
                    break;
                default:
                    builder.say(new Say.Builder("Sorry, I don\'t understand that choice.").build());
                    appendGather(builder);
                    break;
            }
        } else {
            appendGather(builder);
        }
        return builder.build();
    }

}
