package com.example.backend_spring.entity;

import com.example.backend_spring.security.PiiEncryptor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
@Getter
@Setter
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 20)
    private String gender;

    @Convert(converter = PiiEncryptor.class)
    @Column(length = 255)
    private String dob;

    @Column(length = 100)
    private String fatherName;

    @Column(length = 500)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Convert(converter = PiiEncryptor.class)
    @Column(nullable = false, length = 255)
    private String email;

    @Convert(converter = PiiEncryptor.class)
    @Column(nullable = false, length = 255)
    private String mobile;

    @Column(nullable = false, length = 100)
    private String position;

    @Column(length = 1000)
    private String q10;

    @Column(length = 1000)
    private String q11;

    @Column(length = 1000)
    private String q12;

    @Column(length = 1000)
    private String q13;

    @Column(length = 1000)
    private String q14;

    @Column(length = 2000)
    private String workExperience;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime appliedAt;
}
