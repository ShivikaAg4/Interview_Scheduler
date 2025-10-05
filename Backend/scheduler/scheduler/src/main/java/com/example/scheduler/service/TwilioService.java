package com.example.scheduler.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class TwilioService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhone;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendSms(String toPhone, String message) {
        toPhone = formatPhone(toPhone);
        System.out.println("Sending SMS to: " + toPhone);

        Message.creator(
                new PhoneNumber(toPhone),
                new PhoneNumber(fromPhone),
                message
        ).create();
    }

    public void makeCall(String toPhone, String messageUrl) {
        try {
            toPhone = formatPhone(toPhone);
            System.out.println("Making Call to: " + toPhone);

            Call.creator(
                    new PhoneNumber(toPhone),
                    new PhoneNumber(fromPhone),
                    new URI(messageUrl)
            ).create();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid TwiML URL", e);
        }
    }

    private String formatPhone(String phone) {
        if (phone == null) return null;

        phone = phone.trim().replaceAll("\\s+", "").replaceFirst("^0+", "");
        if (phone.startsWith("+")) return phone;          // already formatted
        if (phone.matches("^[789]\\d{9}$")) return "+91" + phone;

        throw new IllegalArgumentException("Invalid phone number: " + phone);
    }
}
