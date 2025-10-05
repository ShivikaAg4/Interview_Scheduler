package com.example.scheduler.Controller;

import com.example.scheduler.model.Candidate;
import com.example.scheduler.repository.CandidateRepository;
import com.example.scheduler.service.TwilioService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private CandidateRepository candidateRepo;

    @Autowired
    private TwilioService twilioService;

    @PostMapping("/sms/{email}")
    public ResponseEntity<String> sendSms(@PathVariable String email) {
        Optional<Candidate> optionalCandidate = candidateRepo.findByEmail(email);
        if (optionalCandidate.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate not found.");
        }

        Candidate candidate = optionalCandidate.get();
        String phone = formatPhone(candidate.getPhone());

        try {
            String message = "Dear " + candidate.getName() +
                    ", your interview schedule will be shared shortly. Stay tuned!";
            twilioService.sendSms(phone, message);
            return ResponseEntity.ok("SMS sent to " + candidate.getName());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send SMS: " + e.getMessage());
        }
    }

    @PostMapping("/call/{email}")
    public ResponseEntity<String> makeCall(@PathVariable String email) {
        Optional<Candidate> optionalCandidate = candidateRepo.findByEmail(email);
        if (optionalCandidate.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate not found.");
        }

        Candidate candidate = optionalCandidate.get();
        String phone = formatPhone(candidate.getPhone());

        try {
            String messageUrl = UriComponentsBuilder
                    .fromHttpUrl("https://your-server.com/twilio/voice") // TODO: Replace with real public URL (ngrok or prod)
                    .queryParam("name", candidate.getName())
                    .queryParam("date", "2025-07-30")
                    .queryParam("time", "11AM")
                    .queryParam("mode", "Online")
                    .toUriString();

            twilioService.makeCall(phone, messageUrl);
            return ResponseEntity.ok("Call initiated for " + candidate.getName());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to initiate call: " + e.getMessage());
        }
    }
    private String formatPhone(String phone) {
        // Remove all whitespaces and leading 0s
        phone = phone.trim().replaceAll("\\s+", "").replaceFirst("^0+", "");

        // If number already starts with '+', return as-is
        if (phone.startsWith("+")) {
            return phone;
        }

        // Basic validation for Indian 10-digit numbers
        if (phone.matches("^[789]\\d{9}$")) {
            return "+91" + phone;
        }

        throw new IllegalArgumentException("Invalid phone number format: " + phone);
    }

}
