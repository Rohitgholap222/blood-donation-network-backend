package com.bloodnetwork.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "donor_profiles", indexes = {
    @Index(name = "idx_blood_group", columnList = "bloodGroup"),
    @Index(name = "idx_available", columnList = "isAvailable")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonorProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BloodGroup bloodGroup;

    private Double latitude;
    private Double longitude;

    @Column(nullable = false)
    private Boolean isAvailable;

    private LocalDate lastDonationDate;
}
