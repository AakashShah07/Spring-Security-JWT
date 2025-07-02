package com.example.User_JWT.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vaidya extends User{

    private String specialization;
    private String clinicId;
    private int experienceYears;
    private double consultationFee;
    private double ratings;
    private int totalConsultations;
    private List<String> badges;

}
