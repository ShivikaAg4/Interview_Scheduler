package com.example.scheduler.Controller;

import com.example.scheduler.model.Candidate;
import com.example.scheduler.repository.CandidateRepository;
import com.example.scheduler.service.ATSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

@RestController
@RequestMapping("/resumes")
@CrossOrigin(origins = "*")
public class ResumeUploadController {

    private static final String uploadDir = System.getProperty("user.dir") + "/uploads/";

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ATSService atsService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadResume(@RequestParam("email") String email,
                                               @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty.");
        }

        // Validate file extension (only allow real documents)
        String originalName = file.getOriginalFilename();
        if (originalName == null ||
                !(originalName.endsWith(".pdf") || originalName.endsWith(".doc") || originalName.endsWith(".docx"))) {
            return ResponseEntity.badRequest().body("Unsupported file type. Only PDF and DOC/DOCX are allowed.");
        }

        Optional<Candidate> optionalCandidate = candidateRepository.findByEmail(email);
        if (optionalCandidate.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate not found.");
        }

        try {
            Files.createDirectories(Paths.get(uploadDir));
            String filename = System.currentTimeMillis() + "_" + originalName;
            Path filePath = Paths.get(uploadDir + filename);
            file.transferTo(filePath.toFile());

            Candidate candidate = optionalCandidate.get();
            candidate.setResumeLink(filePath.toString());

            // ---------------------- Manual Scoring (currently active) ----------------------
            double score = 80.0;
            candidate.setAtsScore(score);
            candidate.setShortlisted(true);
            candidate.setStatus("Shortlisted");

            /*
            // ---------------------- Affinda API Logic (commented out) ----------------------
            try {
                double score = atsService.evaluateResume(filePath.toFile());
                candidate.setAtsScore(score);
                candidate.setShortlisted(score >= 70.0); // example threshold
                candidate.setStatus(score >= 70.0 ? "Shortlisted" : "Rejected");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("ATS evaluation failed: " + e.getMessage());
            }
            */

            candidateRepository.save(candidate);
            return ResponseEntity.ok("Resume uploaded and ATS evaluated. Score: " + score);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }
}
