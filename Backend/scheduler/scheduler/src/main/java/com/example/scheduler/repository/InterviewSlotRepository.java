package com.example.scheduler.repository;

import com.example.scheduler.model.InterviewSlot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InterviewSlotRepository extends MongoRepository<InterviewSlot, String> {
    InterviewSlot findByDateAndStartTime(String date, String startTime);
    List<InterviewSlot> findByDate(String date);
    List<InterviewSlot> findByIsBookedFalse();
}
