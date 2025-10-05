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
import java.util.Map;

@Service
public class ResumeParserService {

    private final RestTemplate restTemplate;

    @Value("${affinda.api.key}")
    private String apiKey;

    public ResumeParserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> parseByFile(File file) {
        String apiUrl = "https://api.affinda.com/v2/resumes/parse";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        FileSystemResource fileResource = new FileSystemResource(file) {
            @Override
            public String getFilename() {
                return file.getName(); // must include .pdf/.docx etc.
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            System.out.println("✅ Affinda response: " + response.getBody());
            return response.getBody();

        } catch (HttpClientErrorException e) {
            System.err.println("❌ Affinda error: " + e.getResponseBodyAsString());
            throw new RuntimeException("Affinda parsing failed: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("❌ General Affinda error");
            e.printStackTrace();
            throw new RuntimeException("Affinda request failed: " + e.getMessage());
        }
    }
}
