package com.example.backend_spring.service;

import com.example.backend_spring.dto.JobApplicationDto;
import com.example.backend_spring.entity.JobApplication;
import com.example.backend_spring.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CareerService {

    private final JobApplicationRepository repository;

    public void submitApplication(JobApplicationDto dto) {
        JobApplication application = new JobApplication();
        application.setName(dto.getName());
        application.setGender(dto.getGender());
        application.setDob(dto.getDob());
        application.setFatherName(dto.getFatherName());
        application.setAddress(dto.getAddress());
        application.setCity(dto.getCity());
        application.setState(dto.getState());
        application.setEmail(dto.getEmail());
        application.setMobile(dto.getMobile());
        application.setPosition(dto.getPosition());
        application.setQ10(dto.getQ10());
        application.setQ11(dto.getQ11());
        application.setQ12(dto.getQ12());
        application.setQ13(dto.getQ13());
        application.setQ14(dto.getQ14());
        application.setWorkExperience(dto.getWorkExperience());

        repository.save(application);
    }

    public List<JobApplication> getAllApplications() {
        return repository.findAll();
    }
}
