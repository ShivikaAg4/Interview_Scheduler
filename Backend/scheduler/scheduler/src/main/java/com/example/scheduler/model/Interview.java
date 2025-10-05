package com.example.scheduler.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "interviews")
@Data
public class Interview {
    @Id
    private String id;
    private String candidateName;
    private String email;
    private LocalDateTime scheduledTime;
    private String status;
}