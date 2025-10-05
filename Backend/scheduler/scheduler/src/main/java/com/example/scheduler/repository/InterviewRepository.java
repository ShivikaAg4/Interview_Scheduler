package com.example.scheduler.repository;

import com.example.scheduler.model.Interview;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InterviewRepository extends MongoRepository<Interview, String> {
    List<Interview> findByEmail(String email);
}
