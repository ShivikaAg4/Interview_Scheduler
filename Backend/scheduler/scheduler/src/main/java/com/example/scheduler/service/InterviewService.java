package com.example.scheduler.service;

import com.example.scheduler.model.Interview;
import com.example.scheduler.repository.InterviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewService {
    private final InterviewRepository repo;

    public InterviewService(InterviewRepository repo) {
        this.repo = repo;
    }

    public Interview create(Interview interview) {
        return repo.save(interview);
    }

    public List<Interview> getAll() {
        return repo.findAll();
    }
    public void DeleteInterview(String id){
        repo.deleteById(id);
    }
}
