package com.example.scheduler.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ATSService {

    @Value("${affinda.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public ATSService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public double calculateScoreFromFile(File file) throws IOException {
        String apiUrl = "https://api.affinda.com/v2/resumes";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        FileSystemResource resource = new FileSystemResource(file) {
            @Override
            public String getFilename() {
                return file.getName();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            Map<String, Object> parsedData = response.getBody();
            if (parsedData == null || !parsedData.containsKey("data")) {
                throw new RuntimeException("Invalid Affinda response");
            }

            Map<String, Object> data = (Map<String, Object>) parsedData.get("data");

            int score = 0;
            if (data.containsKey("skills")) {
                List<Map<String, Object>> skills = (List<Map<String, Object>>) data.get("skills");
                if (skills != null) score += Math.min(skills.size() * 5, 50);
            }
            if (data.containsKey("education")) score += 10;
            if (data.containsKey("workExperience")) score += 20;

            return score;

        } catch (HttpClientErrorException e) {
            System.err.println("❌ Affinda API Error: " + e.getResponseBodyAsString());
            throw new RuntimeException("Affinda API Error: " + e.getResponseBodyAsString());
        }
    }
}
