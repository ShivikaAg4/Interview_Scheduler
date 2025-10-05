package com.example.scheduler.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "schedules")
public class Schedule {
    @Id
    private String id;

    private String candidateId;
    private String interviewerName;
    private String interviewDate;
    private String timeSlot; // whatever duration
    private String mode;//  "Online", "Offline"
    private String status; // "Scheduled", "Completed", "Cancelled"

}
