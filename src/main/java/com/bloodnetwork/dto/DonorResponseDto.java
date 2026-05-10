package com.bloodnetwork.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DonorResponseDto implements java.io.Serializable {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String bloodGroup;
    private Double latitude;
    private Double longitude;
    private Boolean isAvailable;
}
