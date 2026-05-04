package com.bloodnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorProfileUpdateRequest {
    private Double latitude;
    private Double longitude;
    private Boolean isAvailable;
    private String bloodGroup;
}
