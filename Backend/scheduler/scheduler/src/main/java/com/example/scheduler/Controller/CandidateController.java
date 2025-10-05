package com.example.scheduler.Controller;

import com.example.scheduler.model.Candidate;
import com.example.scheduler.repository.CandidateRepository;
import com.example.scheduler.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidates")
@CrossOrigin(origins = "*")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;
    @Autowired
    private CandidateRepository candidateRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addCandidate(@RequestBody Candidate candidate) {
        String result = candidateService.addCandidate(candidate);
        if (result.equals("EXISTS")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Candidate already exists");
        }
        return ResponseEntity.ok("Candidate added successfully");
    }

    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }
    @GetMapping("/shortlisted")
    public ResponseEntity<List<Candidate>> getShortlistedCandidates() {
        return ResponseEntity.ok(candidateRepository.findByIsShortlisted(true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInterview(@PathVariable String id) {
        candidateService.deleteCandidtae(id);
        return ResponseEntity.ok("Candidtae deleted successfully");
    }
    @DeleteMapping("/by-email")
    public ResponseEntity<String> deleteByEmail(@RequestParam String email) {
        boolean deleted = candidateService.deleteByEmail(email);
        return deleted ?
                ResponseEntity.ok("Candidate deleted by email") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate not found");
    }

}
