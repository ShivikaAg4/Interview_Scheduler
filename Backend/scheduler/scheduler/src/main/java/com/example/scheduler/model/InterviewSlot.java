package com.example.scheduler.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "interviewSlots")
public class InterviewSlot {
    @Id
    private String id;

    private String date;
    private String startTime;
    private String endTime;
    private boolean isBooked;
}
