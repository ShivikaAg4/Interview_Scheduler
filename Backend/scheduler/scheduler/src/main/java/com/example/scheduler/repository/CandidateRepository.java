package com.example.scheduler.repository;

import com.example.scheduler.model.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends MongoRepository<Candidate, String> {
    Optional<Candidate> findByEmail(String email);
    List<Candidate> findByIsShortlisted(boolean isShortlisted);

}
