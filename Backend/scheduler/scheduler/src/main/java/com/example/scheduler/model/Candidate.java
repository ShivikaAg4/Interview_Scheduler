package com.example.scheduler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "candidates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {
    @Id
    private String id;

    private String name;
    private String email;
    private String phone;
    private String resumeLink;
    private String status; // e.g., "Applied", "Shortlisted", "Rejected"
    private double atsScore;
    private boolean isShortlisted;
}
