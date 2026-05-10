package com.bloodnetwork.controller;

import com.bloodnetwork.dto.ApiResponse;
import com.bloodnetwork.dto.DonorResponseDto;
import com.bloodnetwork.service.DonorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
@RequiredArgsConstructor
public class DonorController {

    private final DonorService donorService;

    @GetMapping("/nearest")
    @PreAuthorize("hasRole('HOSPITAL') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<DonorResponseDto>>> getNearestDonors(
            @RequestParam String bloodGroup,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "10") int limit) {
            
        List<DonorResponseDto> donors = donorService.getNearestDonors(bloodGroup, latitude, longitude, limit);
        return ResponseEntity.ok(new ApiResponse<>(true, "Nearest donors retrieved", donors));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<ApiResponse<DonorResponseDto>> getProfile(
            @io.swagger.v3.oas.annotations.Parameter(hidden = true) @org.springframework.security.core.annotation.AuthenticationPrincipal com.bloodnetwork.security.CustomUserDetails userDetails) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile retrieved", donorService.getMyProfile(userDetails.getUser().getId())));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @io.swagger.v3.oas.annotations.Parameter(hidden = true) @org.springframework.security.core.annotation.AuthenticationPrincipal com.bloodnetwork.security.CustomUserDetails userDetails,
            @RequestBody com.bloodnetwork.dto.DonorProfileUpdateRequest updateRequest) {
        donorService.updateProfile(userDetails.getUser().getId(), updateRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully", null));
    }
}
