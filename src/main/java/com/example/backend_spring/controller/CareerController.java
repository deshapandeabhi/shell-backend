package com.example.backend_spring.controller;

import com.example.backend_spring.dto.JobApplicationDto;
import com.example.backend_spring.entity.JobApplication;
import com.example.backend_spring.service.CareerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/careers")
@RequiredArgsConstructor
public class CareerController {

    private final CareerService careerService;

    @PostMapping("/apply")
    public ResponseEntity<?> submitApplication(@Valid @RequestBody JobApplicationDto dto) {
        careerService.submitApplication(dto);
        return ResponseEntity.ok(Map.of("message", "Application submitted successfully"));
    }

    @GetMapping("/applications")
    public ResponseEntity<List<JobApplication>> getAllApplications() {
        return ResponseEntity.ok(careerService.getAllApplications());
    }
}
