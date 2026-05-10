package com.bloodnetwork.service;

import com.bloodnetwork.dto.DonorResponseDto;
import com.bloodnetwork.entity.DonorProfile;
import com.bloodnetwork.repository.DonorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonorService {

    private final DonorProfileRepository donorProfileRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "nearestDonors", key = "#bloodGroup + '-' + #lat + '-' + #lng")
    public List<DonorResponseDto> getNearestDonors(String bloodGroup, Double lat, Double lng, int limit) {
        List<String> compatibleGroups = com.bloodnetwork.util.BloodGroupMapper.getCompatibleGroups(bloodGroup);
        List<DonorProfile> profiles = donorProfileRepository.findNearestDonors(compatibleGroups, lat, lng, limit);
        return profiles.stream()
                .map(profile -> DonorResponseDto.builder()
                        .id(profile.getId())
                        .name(profile.getUser().getName())
                        .email(profile.getUser().getEmail())
                        .phone(profile.getUser().getPhone())
                        .bloodGroup(profile.getBloodGroup().name())
                        .latitude(profile.getLatitude())
                        .longitude(profile.getLongitude())
                        .isAvailable(profile.getIsAvailable())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DonorProfile getProfileByUserId(Long userId) {
        return donorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Donor profile not found"));
    }

    @Transactional
    public void updateProfile(Long userId, com.bloodnetwork.dto.DonorProfileUpdateRequest updateRequest) {
        DonorProfile profile = getProfileByUserId(userId);
        
        if (updateRequest.getLatitude() != null) profile.setLatitude(updateRequest.getLatitude());
        if (updateRequest.getLongitude() != null) profile.setLongitude(updateRequest.getLongitude());
        if (updateRequest.getIsAvailable() != null) profile.setIsAvailable(updateRequest.getIsAvailable());
        if (updateRequest.getBloodGroup() != null) {
            profile.setBloodGroup(com.bloodnetwork.entity.BloodGroup.valueOf(updateRequest.getBloodGroup()));
        }
        
        donorProfileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public DonorResponseDto getMyProfile(Long userId) {
        DonorProfile profile = getProfileByUserId(userId);
        return DonorResponseDto.builder()
                .id(profile.getId())
                .name(profile.getUser().getName())
                .email(profile.getUser().getEmail())
                .phone(profile.getUser().getPhone())
                .bloodGroup(profile.getBloodGroup().name())
                .latitude(profile.getLatitude())
                .longitude(profile.getLongitude())
                .isAvailable(profile.getIsAvailable())
                .build();
    }
}
