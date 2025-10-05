package com.example.scheduler.service;

import com.example.scheduler.model.Candidate;
import com.example.scheduler.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepo;

    public String addCandidate(Candidate candidate) {
        if (candidateRepo.findByEmail(candidate.getEmail()).isPresent()) {
            return "EXISTS";
        }
        candidate.setStatus("Applied");
        candidateRepo.save(candidate);
        return "ADDED";
    }
    public List<Candidate> getAllCandidates()
    {
        return candidateRepo.findAll();
    }

    public void deleteCandidtae(String id) {
        candidateRepo.deleteById(id);
    }
    public boolean deleteByEmail(String email) {
        Optional<Candidate> candidate = candidateRepo.findByEmail(email);
        if (candidate.isPresent()) {
            candidateRepo.delete(candidate.get());
            return true;
        }
        return false;
    }
}

