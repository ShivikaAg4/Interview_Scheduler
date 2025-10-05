package com.example.scheduler.Controller;

import com.example.scheduler.model.InterviewSlot;
import com.example.scheduler.service.InterviewSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slots")
@CrossOrigin(origins = "*")
public class InterviewSlotController {

    @Autowired
    private InterviewSlotService service;

    @PostMapping
    public ResponseEntity<InterviewSlot> create(@RequestBody InterviewSlot slot) {
        return ResponseEntity.ok(service.create(slot));
    }

    @GetMapping
    public ResponseEntity<List<InterviewSlot>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok("Slot deleted successfully");
    }
    @DeleteMapping("/all")
    public ResponseEntity<String> DeleteAll()
    {
        service.deleteAll();
        return ResponseEntity.ok("all slots deleted successfully");
    }
}
