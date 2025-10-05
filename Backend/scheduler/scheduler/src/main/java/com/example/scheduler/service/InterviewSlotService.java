package com.example.scheduler.service;

import com.example.scheduler.model.InterviewSlot;
import com.example.scheduler.repository.InterviewSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewSlotService {

    @Autowired
    private InterviewSlotRepository repo;

    public InterviewSlot create(InterviewSlot slot) {
        return repo.save(slot);
    }

    public List<InterviewSlot> getAll() {
        return repo.findAll();
    }

    public void delete(String id) {
        repo.deleteById(id);
    }

    public void deleteAll(){repo.deleteAll();}
}
