package com.example.scheduler.Controller;

import com.example.scheduler.dto.ScheduleRequest;
import com.example.scheduler.dto.ScheduleResponse;
import com.example.scheduler.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {

    @Autowired
    private ScheduleService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ScheduleRequest request) {
        try {
            ScheduleResponse response = service.createSchedule(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ScheduleRequest request) {
        try {
            ScheduleResponse updated = service.updateSchedule(id, request);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getAll() {
        return ResponseEntity.ok(service.getAllSchedules());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ScheduleResponse>> filterSchedules(
            @RequestParam(required = false) String candidateId,
            @RequestParam(required = false) String interviewerName,
            @RequestParam(required = false) String interviewDate,
            @RequestParam(required = false) String mode
    ) {
        return ResponseEntity.ok(service.filterSchedules(candidateId, interviewerName, interviewDate, mode));
    }
}
