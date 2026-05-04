package com.bloodnetwork.controller;

import com.bloodnetwork.dto.ApiResponse;
import com.bloodnetwork.dto.DonorProfileUpdateRequest;
import com.bloodnetwork.entity.DonorProfile;
import com.bloodnetwork.security.CustomUserDetails;
import com.bloodnetwork.service.DonorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/donor-profile")
@RequiredArgsConstructor
public class DonorProfileController {

    private final DonorService donorService;

    @GetMapping
    public ResponseEntity<ApiResponse<DonorProfile>> getMyProfile(
            @io.swagger.v3.oas.annotations.Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails) {
        DonorProfile profile = donorService.getProfileByUserId(userDetails.getUser().getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile retrieved", profile));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @io.swagger.v3.oas.annotations.Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody DonorProfileUpdateRequest updateRequest) {
        donorService.updateProfile(userDetails.getUser().getId(), updateRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully", null));
    }
}
