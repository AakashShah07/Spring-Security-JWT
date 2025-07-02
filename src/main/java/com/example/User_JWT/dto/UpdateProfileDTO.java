package com.example.User_JWT.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdateProfileDTO {
    // Common fields
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime dateOfBirth;
    private String gender;
    private String prakritiType;

    // Vaidya-specific fields (optional)
    private String specialization;
    private String clinicId;
    private Integer experienceYears;
    private Double consultationFee;
    private Double ratings;
    private List<String> badges;
}
