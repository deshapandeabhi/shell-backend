package com.example.backend_spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobApplicationDto {

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must only contain alphabetic characters and spaces")
    private String name;

    private String gender;

    private String dob;

    private String fatherName;

    private String address;

    private String city;

    private String state;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Mobile number must be between 10 and 15 digits")
    private String mobile;

    @NotBlank(message = "Position is required")
    private String position;

    private String q10;
    private String q11;
    private String q12;
    private String q13;
    private String q14;
    private String workExperience;
}
