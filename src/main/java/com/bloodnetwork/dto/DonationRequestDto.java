package com.bloodnetwork.dto;

import com.bloodnetwork.entity.BloodGroup;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DonationRequestDto {
    @NotNull
    private BloodGroup bloodGroup;
    @NotNull
    private Integer unitsRequired;
    
    private String urgency;
    private Double latitude;
    private Double longitude;
}
