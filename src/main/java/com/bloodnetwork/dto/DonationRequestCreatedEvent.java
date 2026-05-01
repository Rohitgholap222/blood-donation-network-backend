package com.bloodnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DonationRequestCreatedEvent {
    private Long requestId;
    private String bloodGroup;
    private Double latitude;
    private Double longitude;
    private String hospitalName;
}
