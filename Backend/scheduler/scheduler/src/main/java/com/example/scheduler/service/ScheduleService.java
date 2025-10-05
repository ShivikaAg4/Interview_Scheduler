package com.example.scheduler.service;

import com.example.scheduler.dto.ScheduleRequest;
import com.example.scheduler.dto.ScheduleResponse;
import com.example.scheduler.model.*;
import com.example.scheduler.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class  ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepo;

    @Autowired
    private CandidateRepository candidateRepo;

    @Autowired
    private InterviewSlotRepository slotRepo;

    // ---------------- CREATE ----------------
    public ScheduleResponse createSchedule(ScheduleRequest req) {
        Candidate candidate = candidateRepo.findById(req.getCandidateId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid candidate ID"));

        InterviewSlot slot = slotRepo.findById(req.getInterviewSlotId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid interview slot ID"));

        if (slot.isBooked()) {
            throw new IllegalArgumentException("Interview slot is already booked.");
        }

        slot.setBooked(true);
        slotRepo.save(slot);

        Schedule schedule = new Schedule();
        schedule.setCandidateId(candidate.getId());
        schedule.setInterviewerName(req.getInterviewerName());
        schedule.setInterviewDate(slot.getDate());
        schedule.setTimeSlot(slot.getStartTime() + " - " + slot.getEndTime());
        schedule.setMode(req.getMode());
        schedule.setStatus("Scheduled");

        Schedule saved = scheduleRepo.save(schedule);
        return mapToResponse(saved, candidate, slot);
    }

    // ---------------- UPDATE ----------------
    public ScheduleResponse updateSchedule(String id, ScheduleRequest req) {
        Schedule existing = scheduleRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        Candidate candidate = candidateRepo.findById(req.getCandidateId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid candidate ID"));

        InterviewSlot newSlot = slotRepo.findById(req.getInterviewSlotId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid interview slot ID"));

        // If slot changes, free old one and book new
        if (!existing.getInterviewDate().equals(newSlot.getDate()) ||
                !existing.getTimeSlot().contains(newSlot.getStartTime())) {

            // Unbook old slot
            slotRepo.findAll().stream()
                    .filter(s -> existing.getTimeSlot().contains(s.getStartTime()) &&
                            existing.getInterviewDate().equals(s.getDate()))
                    .findFirst()
                    .ifPresent(s -> {
                        s.setBooked(false);
                        slotRepo.save(s);
                    });

            // Book new slot
            if (newSlot.isBooked()) {
                throw new IllegalArgumentException("New interview slot is already booked.");
            }
            newSlot.setBooked(true);
            slotRepo.save(newSlot);
        }

        existing.setCandidateId(candidate.getId());
        existing.setInterviewerName(req.getInterviewerName());
        existing.setInterviewDate(newSlot.getDate());
        existing.setTimeSlot(newSlot.getStartTime() + " - " + newSlot.getEndTime());
        existing.setMode(req.getMode());
        existing.setStatus("Scheduled");

        Schedule updated = scheduleRepo.save(existing);
        return mapToResponse(updated, candidate, newSlot);
    }

    // ---------------- GET ----------------
    public List<ScheduleResponse> getAllSchedules() {
        return scheduleRepo.findAll().stream()
                .map(this::mapToResponseWithLookup)
                .collect(Collectors.toList());
    }

    public List<ScheduleResponse> filterSchedules(String candidateId, String interviewerName, String interviewDate, String mode) {
        return scheduleRepo.findAll().stream()
                .filter(schedule ->
                        (candidateId == null || schedule.getCandidateId().equals(candidateId)) &&
                                (interviewerName == null || schedule.getInterviewerName().equalsIgnoreCase(interviewerName)) &&
                                (interviewDate == null || schedule.getInterviewDate().equals(interviewDate)) &&
                                (mode == null || schedule.getMode().equalsIgnoreCase(mode))
                )
                .map(this::mapToResponseWithLookup)
                .collect(Collectors.toList());
    }

    // ---------------- DELETE ----------------
    public void delete(String id) {
        scheduleRepo.deleteById(id);
    }

    // ---------------- MAPPING ----------------
    private ScheduleResponse mapToResponse(Schedule schedule, Candidate candidate, InterviewSlot slot) {
        ScheduleResponse response = new ScheduleResponse();
        response.setId(schedule.getId());
        response.setCandidate(candidate);
        response.setInterviewSlot(slot);
        response.setInterviewerName(schedule.getInterviewerName());
        response.setInterviewDate(schedule.getInterviewDate());
        response.setTimeSlot(schedule.getTimeSlot());
        response.setMode(schedule.getMode());
        response.setStatus(schedule.getStatus());
        return response;
    }

    private ScheduleResponse mapToResponseWithLookup(Schedule schedule) {
        Candidate candidate = candidateRepo.findById(schedule.getCandidateId()).orElse(null);

        InterviewSlot slot = slotRepo.findAll().stream()
                .filter(s -> schedule.getTimeSlot() != null &&
                        schedule.getTimeSlot().contains(s.getStartTime()) &&
                        schedule.getInterviewDate().equals(s.getDate()))
                .findFirst().orElse(null);

        return mapToResponse(schedule, candidate, slot);
    }
}
