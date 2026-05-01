package com.bloodnetwork.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DonorResponseDto {
    private Long id;
    private String name;
    private String phone;
    private String bloodGroup;
    private Double latitude;
    private Double longitude;
}
