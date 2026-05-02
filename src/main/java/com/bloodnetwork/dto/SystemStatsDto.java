package com.bloodnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemStatsDto {
    private long totalDonors;
    private long totalHospitals;
    private long activeRequests;
    private long successfulDonations;
}
