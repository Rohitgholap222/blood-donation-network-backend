package com.bloodnetwork.controller;

import com.bloodnetwork.dto.ApiResponse;
import com.bloodnetwork.dto.DonationRequestDto;
import com.bloodnetwork.entity.DonationRequest;
import com.bloodnetwork.entity.User;
import com.bloodnetwork.security.CustomUserDetails;
import com.bloodnetwork.service.DonationRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationRequestService donationRequestService;

    @PostMapping("/request")
    @PreAuthorize("hasRole('HOSPITAL')")
    public ResponseEntity<ApiResponse<DonationRequest>> createRequest(
            @Valid @RequestBody DonationRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
            
        User hospital = userDetails.getUser();
        DonationRequest request = donationRequestService.createRequest(requestDto, hospital);
        return ResponseEntity.ok(new ApiResponse<>(true, "Donation request created", request));
    }
}
