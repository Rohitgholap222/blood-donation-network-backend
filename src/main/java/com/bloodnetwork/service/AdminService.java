package com.bloodnetwork.service;

import com.bloodnetwork.dto.SystemStatsDto;
import com.bloodnetwork.entity.DonationRequest;
import com.bloodnetwork.entity.Role;
import com.bloodnetwork.entity.User;
import com.bloodnetwork.repository.DonationRequestRepository;
import com.bloodnetwork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final DonationRequestRepository donationRequestRepository;

    public SystemStatsDto getSystemStats() {
        return SystemStatsDto.builder()
                .totalDonors(userRepository.countByRole(Role.DONOR))
                .totalHospitals(userRepository.countByRole(Role.HOSPITAL))
                .activeRequests(donationRequestRepository.countByStatus("PENDING"))
                .successfulDonations(donationRequestRepository.countByStatus("COMPLETED"))
                .build();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<DonationRequest> getAllActiveRequests() {
        return donationRequestRepository.findByStatus("PENDING");
    }
}
