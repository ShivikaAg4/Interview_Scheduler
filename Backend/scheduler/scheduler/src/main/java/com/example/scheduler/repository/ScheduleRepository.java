package com.example.scheduler.repository;

import com.example.scheduler.model.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    List<Schedule> findByCandidateId(String candidateId);
    List<Schedule> findByInterviewerName(String interviewerName);
    List<Schedule> findByInterviewDate(String interviewDate);
    List<Schedule> findByMode(String mode);
    List<Schedule> findByInterviewDateAndMode(String interviewDate, String mode);

}
