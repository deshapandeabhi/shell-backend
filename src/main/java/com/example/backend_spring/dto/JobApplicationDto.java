package com.example.backend_spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobApplicationDto {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must only contain alphabetic characters and spaces")
    private String name;

    @Size(max = 20, message = "Gender must not exceed 20 characters")
    private String gender;

    @Size(max = 50, message = "DOB must not exceed 50 characters")
    private String dob;

    @Size(max = 100, message = "Father's name must not exceed 100 characters")
    private String fatherName;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Size(max = 20, message = "Mobile number must not exceed 20 characters")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Mobile number must be between 10 and 15 digits")
    private String mobile;

    @NotBlank(message = "Position is required")
    @Size(max = 100, message = "Position must not exceed 100 characters")
    private String position;

    @Size(max = 1000, message = "Response must not exceed 1000 characters")
    private String q10;
    @Size(max = 1000, message = "Response must not exceed 1000 characters")
    private String q11;
    @Size(max = 1000, message = "Response must not exceed 1000 characters")
    private String q12;
    @Size(max = 1000, message = "Response must not exceed 1000 characters")
    private String q13;
    @Size(max = 1000, message = "Response must not exceed 1000 characters")
    private String q14;

    @Size(max = 2000, message = "Work experience must not exceed 2000 characters")
    private String workExperience;
}
