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
                        .phone(profile.getUser().getPhone())
                        .bloodGroup(profile.getBloodGroup().name())
                        .latitude(profile.getLatitude())
                        .longitude(profile.getLongitude())
                        .build())
                .collect(Collectors.toList());
    }
}
