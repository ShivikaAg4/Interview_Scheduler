package com.example.scheduler.dto;

import com.example.scheduler.model.Candidate;
import com.example.scheduler.model.InterviewSlot;
import lombok.Data;

@Data
public class ScheduleResponse {
    private String id;
    private Candidate candidate;
    private InterviewSlot interviewSlot;
    private String interviewerName;
    private String interviewDate;
    private String timeSlot;
    private String mode;
    private String status;
}
