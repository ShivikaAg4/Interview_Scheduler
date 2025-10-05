package com.example.scheduler.Controller;

import com.example.scheduler.model.Interview;
import com.example.scheduler.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interviews")
public class InterviewController {
    private final InterviewService service;

    public InterviewController(InterviewService service)
    {
        this.service = service;
    }

    @PostMapping
    public Interview schedule(@RequestBody Interview i)
    {
        return service.create(i);
    }

    @GetMapping
    public List<Interview> all()
    {
        return service.getAll();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInterview(@PathVariable String id)
    {
        service.DeleteInterview(id);
        return ResponseEntity.ok("Interview deleted successfully");
    }
}
