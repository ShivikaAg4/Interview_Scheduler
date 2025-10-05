package com.example.scheduler.dto;

import lombok.Data;

@Data
public class ScheduleRequest {
    private String candidateId;
    private String interviewSlotId;
    private String interviewerName;
    private String mode;
}
