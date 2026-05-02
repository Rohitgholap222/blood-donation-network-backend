package com.bloodnetwork.controller;

import com.bloodnetwork.dto.ApiResponse;
import com.bloodnetwork.dto.SystemStatsDto;
import com.bloodnetwork.entity.DonationRequest;
import com.bloodnetwork.entity.User;
import com.bloodnetwork.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<SystemStatsDto>> getStats() {
        return ResponseEntity.ok(new ApiResponse<>(true, "System stats retrieved", adminService.getSystemStats()));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse<>(true, "All users retrieved", adminService.getAllUsers()));
    }

    @GetMapping("/requests")
    public ResponseEntity<ApiResponse<List<DonationRequest>>> getAllActiveRequests() {
        return ResponseEntity.ok(new ApiResponse<>(true, "All active requests retrieved", adminService.getAllActiveRequests()));
    }
}
