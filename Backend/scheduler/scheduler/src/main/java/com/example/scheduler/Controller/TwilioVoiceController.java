package com.example.scheduler.Controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twilio")
public class TwilioVoiceController {

    @GetMapping(value = "/voice", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> voice(@RequestParam String name,
                                        @RequestParam String date,
                                        @RequestParam String time,
                                        @RequestParam String mode) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Response>\n" +
                "<Say voice=\"alice\">Hello " + name + ". Your interview is scheduled on " + date +
                " at " + time + ". The mode of interview is " + mode + ". Good luck!</Say>\n" +
                "</Response>";
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(xml);
    }
}
