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
}
